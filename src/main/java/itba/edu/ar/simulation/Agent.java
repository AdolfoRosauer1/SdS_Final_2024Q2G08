package itba.edu.ar.simulation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Agent {
    // Simulation parameters
    private final Config config;
    private final List<Agent> agents;

    // Agent identification
    private final int id;
    private AgentType type;

    // Agent state
    private boolean isInContact;
    private double contactStartTime;
    private Agent contactAgent;

    // Position and movement
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D desiredDirection;
    private double speed;

    // Physical properties
    private double radius;
    private final double minRadius;
    private final double maxRadius;
    private final double relaxationTime;
    private final double CPM_BETA;

    // List of contacts with other agents
    private final List<Agent> contacts;

    public Agent(int id, AgentType type, List<Agent> agents, Config config) {
        this.id = id;
        this.type = type;
        this.speed = type == AgentType.HUMAN ? config.getHumanSpeed() : config.getZombieSpeed();
        this.isInContact = false;
        this.contactStartTime = 0.0;
        this.contactAgent = null;
        this.minRadius = config.getMinRadius();
        this.radius = config.getMaxRadius();
        this.maxRadius = config.getMaxRadius();
        this.relaxationTime = config.getRelaxationTime();
        this.CPM_BETA = config.getCpmBeta();
        this.agents = agents;
        this.config = config;

        // Inicializar posición aleatoria dentro del área permitida
        this.position = PositionInitializer.initializePosition(config, agents, this);
        this.velocity = new Vector2D(0, 0);
        this.desiredDirection = new Vector2D(0, 0);
        this.contacts = new ArrayList<>();
    }

    public Agent(Agent agent) {
        this.id = agent.id;
        this.type = agent.type;
        this.speed = agent.speed;
        this.isInContact = agent.isInContact;
        this.contactStartTime = agent.contactStartTime;
        this.contactAgent = agent.contactAgent;
        this.position = new Vector2D(agent.position);
        this.velocity = new Vector2D(agent.velocity);
        this.desiredDirection = new Vector2D(agent.desiredDirection);
        this.radius = agent.radius;
        this.minRadius = agent.minRadius;
        this.maxRadius = agent.maxRadius;
        this.relaxationTime = agent.relaxationTime;
        this.contacts = new ArrayList<>(agent.contacts);
        this.agents = agent.agents;
        this.config = agent.config;
        this.CPM_BETA = agent.getCPM_BETA();
    }

    public void setCPMVelocity() {
        // If agent is in infection period, velocity should be zero
        if (isInContact && contactAgent != null &&
                (config.getCurrentTime() - contactStartTime < config.getContactDuration())) {
            this.velocity = new Vector2D(0, 0);
            return;
        }

        if (contacts.isEmpty()) {
            // Free movement - velocity should be proportional to normalized radius
            double normalizedRadius = (this.radius - this.minRadius) / (this.maxRadius - this.minRadius);
            // Clamp normalized radius between 0 and 1
            normalizedRadius = Math.max(0, Math.min(1, normalizedRadius));
            // Key difference: Use power function directly with normalized radius
            double desiredSpeed = this.speed * Math.pow(normalizedRadius, CPM_BETA);

            if (desiredDirection.magnitude() < 1e-10) {
                double angle = Math.random() * 2 * Math.PI;
                this.desiredDirection = new Vector2D(Math.cos(angle), Math.sin(angle));
            }

            if (desiredDirection.magnitude() < 1e-10) {
                this.velocity = new Vector2D(0, 0);
                return;
            }

            Vector2D normalizedDirection = desiredDirection.normalize();
            this.velocity = normalizedDirection.multiply(desiredSpeed);
        } else {
            // Escape velocity when in contact - use maximum speed
            Vector2D escapeDirection = new Vector2D(0, 0);
            for (Agent other : contacts) {
                Vector2D diff = this.position.subtract(other.position);
                double distance = diff.magnitude();
                if (distance < 1e-10)
                    distance = 1e-10;
                escapeDirection = escapeDirection.add(diff.divide(distance)); // Normalized direction
            }

            if (escapeDirection.magnitude() > 1e-10) {
                escapeDirection = escapeDirection.normalize();
                // Key difference: Use full speed for escape velocity
                this.velocity = escapeDirection.multiply(this.speed);
            } else {
                this.velocity = new Vector2D(0, 0);
            }
            // Don't clear contacts here - it's handled in calculateRadius
        }
    }

    public void calculateRadius(double dt) {
        contacts.clear();

        for (Agent other : agents) {
            if (other == this)
                continue;

            Vector2D diff = other.position.subtract(this.position);
            double distance = diff.magnitude();
            if (distance < 1e-10)
                distance = 1e-10;

            double combinedRadii = this.radius + other.radius;
            double collisionDistance = combinedRadii * 1.0;

            if (distance < collisionDistance) {
                contacts.add(other);

                this.contract();

                if (!other.getContacts().contains(this)) {
                    other.addContact(this);
                }

                // Handle infection logic
                // Only infect if the other agent is not already in contact
                if (this.type != other.type && !other.isInContact() && !this.isInContact) {
                    if (!this.isInContact || this.contactAgent != other) {
                        // New contact with different type - set contact state for both agents
                        this.isInContact = true;
                        this.contactStartTime = config.getCurrentTime();
                        this.contactAgent = other;

                        other.setInContact(true);
                        other.setContactStartTime(config.getCurrentTime());
                        other.setContactAgent(this);
                    }
                }

                other.contract();
                this.contract();
            }
        }

        // Handle radius changes
        if (contacts.isEmpty()) {
            // No contacts - expand radius
            this.isInContact = false;
            this.contactAgent = null;
            this.radius += (this.maxRadius - this.radius) * (dt / this.relaxationTime);
            this.radius = Math.min(this.radius, this.maxRadius);
        }
    }

    public void updatePosition(double dt) {
        if (isInInfectionPeriod()) {
            handleInfectionFreeze();
            return;
        }
        handleInfection();
        // Update position based on velocity
        setCPMVelocity();
        Vector2D newPosition = this.position.add(this.velocity.multiply(dt));

        // Improved boundary handling
        double distanceFromCenter = newPosition.magnitude();
        if (distanceFromCenter > config.getArenaRadius() - this.radius) {
            // Reflect the position and velocity off the boundary
            Vector2D normal = newPosition.normalize();
            newPosition = normal.multiply(config.getArenaRadius() - this.radius);
            this.velocity = this.velocity.subtract(normal.multiply(2 * this.velocity.dot(normal)));
            this.position = newPosition;
        } else {
            this.position = newPosition;
        }

        calculateRadius(dt);
        updateDesiredDirection();
    }

    private void handleInfection() {
        if (!isInInfectionPeriod() && isInContact) {
            // Validate contactAgent is not null before using it
            if (contactAgent == null) {
                return;
            }

            double turnRoll = Math.random();
            if (turnRoll < config.getProbabilityInfection()) {
                // Turn both agents to Zombie
                this.type = AgentType.ZOMBIE;
                this.speed = config.getZombieSpeed();
                contactAgent.setType(AgentType.ZOMBIE);
                contactAgent.setSpeed(config.getZombieSpeed());
            } else {
                // Turn both agents to Human
                this.type = AgentType.HUMAN;
                this.speed = config.getHumanSpeed();
                contactAgent.setType(AgentType.HUMAN);
                contactAgent.setSpeed(config.getHumanSpeed());
            }

            // Reset contact state for both agents
            this.isInContact = false;
            contactAgent.setInContact(false);
            contactAgent.setContactAgent(null);
            this.contactAgent = null;
        }
    }

    public boolean isInInfectionPeriod() {
        return isInContact && contactAgent != null &&
                (config.getCurrentTime() - contactStartTime < config.getContactDuration());
    }

    public void handleInfectionFreeze() {
        // If agent is in infection period, velocity should be zero
        if (isInInfectionPeriod()) {
            this.velocity = new Vector2D(0, 0);
        }
    }

    public void updateDesiredDirection() {
        if (this.type == AgentType.HUMAN) {

            Vector2D totalDirection = new Vector2D(0, 0);

            // Create lists of humans and zombies with distances
            List<Map.Entry<Agent, Double>> humans = new ArrayList<>();
            List<Map.Entry<Agent, Double>> zombies = new ArrayList<>();

            for (Agent other : agents) {
                if (other == this
                // || other.isInContact()
                )
                    continue;

                Vector2D diff = this.position.subtract(other.position);
                double distance = diff.magnitude();
                if (distance < 1e-10)
                    continue;

                if (other.getType() == AgentType.ZOMBIE) {
                    zombies.add(new AbstractMap.SimpleEntry<>(other, distance));
                } else {
                    humans.add(new AbstractMap.SimpleEntry<>(other, distance));
                }
            }

            // Sort by distance and take nearest nH humans and nZ zombies
            humans.sort(Map.Entry.comparingByValue());
            zombies.sort(Map.Entry.comparingByValue());

            int nH = (int) config.getnH();
            int nZ = (int) config.getnZ();

            humans = humans.subList(0, Math.min(nH, humans.size()));
            zombies = zombies.subList(0, Math.min(nZ, zombies.size()));

            // Calculate total direction based on nearest agents
            for (Map.Entry<Agent, Double> entry : humans) {
                Agent other = entry.getKey();
                double distance = entry.getValue();
                Vector2D direction = this.position.subtract(other.position).normalize();
                totalDirection = totalDirection
                        .add(direction.multiply(config.getAh() * Math.exp(-distance / config.getBh())));
            }

            for (Map.Entry<Agent, Double> entry : zombies) {
                Agent other = entry.getKey();
                double distance = entry.getValue();
                Vector2D direction = this.position.subtract(other.position).normalize();
                totalDirection = totalDirection
                        .add(direction.multiply(config.getAz() * Math.exp(-distance / config.getBz())));
            }

            // Boundary repulsion
            Vector2D boundaryDirection = calculateDistanceToWall();
            totalDirection = totalDirection.add(boundaryDirection.normalize().multiply(config.getAw() *
                    Math.exp(-boundaryDirection.magnitude() / config.getBw())));

            // Need to study if we add noise or not
            // Noise
            // Maximum noise is 1.5 degrees to each side
            double noise = 3 * Math.PI / 180;
            double angularNoise = (Math.random() - 0.5) * noise;
            totalDirection = totalDirection.rotate(angularNoise);
            this.desiredDirection = totalDirection.normalize();

        } else {
            // Zombie behavior - pursue nearest human
            Agent nearestHuman = null;
            double minDistance = Double.MAX_VALUE;

            // Only pursue humans that are not in contact
            for (Agent other : agents) {
                if (other.getType() == AgentType.HUMAN && !other.isInContact()) {
                    double distance = this.position.subtract(other.position).magnitude();
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestHuman = other;
                    }
                }
            }

            if (nearestHuman != null) {
                Vector2D pursuitDirection = nearestHuman.getPosition().subtract(this.position);
                if (pursuitDirection.magnitude() > 1e-10) {
                    this.desiredDirection = pursuitDirection.normalize();
                }
            } else {
                // No humans to pursue, dont move
                this.desiredDirection = new Vector2D(0, 0);
            }
        }
    }

    private Vector2D calculateDistanceToWall() {
        double distanceToCenter = position.magnitude();
        double arenaRadius = config.getArenaRadius();

        // If agent is at center, return zero vector
        if (distanceToCenter < 1e-10) {
            return new Vector2D(0, 0);
        }

        // Calculate vector from center to agent position (normalized)
        Vector2D centerToAgent = position.normalize();

        // Calculate closest point on wall
        Vector2D closestPointOnWall = centerToAgent.multiply(arenaRadius);

        // Vector from agent to closest point on wall
        Vector2D distanceToWall = closestPointOnWall.subtract(position);

        return distanceToWall.multiply(-1.0);
    }

    public void contract() {
        this.radius = this.minRadius;
    }

    public void addContact(Agent agent) {
        this.contacts.add(agent);
    }

    // ==========================
    // Getters and setters
    // ==========================

    public int getId() {
        return id;
    }

    public AgentType getType() {
        return type;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isInContact() {
        return isInContact;
    }

    public void setInContact(boolean inContact) {
        isInContact = inContact;
    }

    public double getContactStartTime() {
        return contactStartTime;
    }

    public void setContactStartTime(double contactStartTime) {
        this.contactStartTime = contactStartTime;
    }

    public Agent getContactAgent() {
        return contactAgent;
    }

    public void setContactAgent(Agent contactAgent) {
        this.contactAgent = contactAgent;
    }

    public Config getConfig() {
        return config;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setType(AgentType type) {
        this.type = type;
    }

    public Vector2D getDesiredDirection() {
        return desiredDirection;
    }

    public void setDesiredDirection(Vector2D desiredDirection) {
        this.desiredDirection = desiredDirection;
    }

    public double getMinRadius() {
        return minRadius;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public double getRelaxationTime() {
        return relaxationTime;
    }

    public double getCPM_BETA() {
        return CPM_BETA;
    }

    public List<Agent> getContacts() {
        return contacts;
    }
}
