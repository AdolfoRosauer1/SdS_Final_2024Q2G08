package itba.edu.ar.simulation;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    // Agent identification
    private int id;
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
    private double minRadius;
    private double maxRadius;
    private double relaxationTime;
    private final double CPM_BETA;

    // List of contacts with other agents
    private List<Agent> contacts;

    public Agent(int id, AgentType type, double speed, double arenaRadius, double minRadius, double maxRadius, double relaxationTime, double cpmBeta) {
        this.id = id; // Initialize id
        this.type = type;
        this.speed = speed;
        this.isInContact = false;
        this.contactStartTime = 0.0;
        this.contactAgent = null;
        this.minRadius = minRadius;
        this.radius = maxRadius;
        this.maxRadius = maxRadius;
        this.relaxationTime = relaxationTime;
        this.CPM_BETA = cpmBeta;

        // Inicializar posición aleatoria dentro del área permitida
        this.position = PositionInitializer.initializePosition(arenaRadius);
        this.velocity = new Vector2D(0, 0);
        this.desiredDirection = new Vector2D(0, 0); // Initialize desiredDirection
        this.contacts = new ArrayList<>(); // Initialize contacts list
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
        this.CPM_BETA = agent.CPM_BETA;
        this.contacts = new ArrayList<>(agent.contacts);
    }

    public void setCPMVelocity() {
        if (contacts.isEmpty()) {
            // Free movement - velocity proportional to available space
            double normalizedRadius = (this.radius - this.minRadius) / (this.maxRadius - this.minRadius);
            normalizedRadius = Math.max(0, Math.min(1, normalizedRadius));
            double desiredSpeed = this.speed * Math.pow(normalizedRadius, CPM_BETA);

            if (desiredDirection.magnitude() < 1e-10) {
                this.velocity = new Vector2D(0, 0);
                return;
            }

            Vector2D normalizedDirection = desiredDirection.normalize();
            this.velocity = normalizedDirection.multiply(desiredSpeed);
        } else {
            // Escape velocity when in contact
            Vector2D escapeDirection = new Vector2D(0, 0);
            for (Agent other : contacts) {
                Vector2D diff = this.position.subtract(other.position);
                if (diff.magnitude() < 1e-10) continue;
                escapeDirection = escapeDirection.add(diff.normalize());
            }

            if (escapeDirection.magnitude() > 1e-10) {
                escapeDirection = escapeDirection.normalize();
                this.velocity = escapeDirection.multiply(this.speed);
            } else {
                this.velocity = new Vector2D(0, 0);
            }
            contacts.clear();
        }
    }

    public void calculateRadius(double dt, List<Agent> agents) {
        for (Agent other : agents) {
            if (other == this) continue;
            
            Vector2D diff = other.position.subtract(this.position);
            double distance = diff.magnitude();
            if (distance < 1e-10) distance = 1e-10;

            if (distance < (this.radius + other.radius)) {
                contacts.add(other);
                other.addContact(this);
                other.contract();
            }
        }

        if (contacts.isEmpty()) {
            // Expand radius
            this.radius += (this.maxRadius - this.radius) * (dt / this.relaxationTime);
            this.radius = Math.min(this.radius, this.maxRadius);
        } else {
            // Contract radius
            contract();
        }
    }

    public void updatePosition(double dt) {
        // Update position based on velocity
        this.position = this.position.add(velocity.multiply(dt));
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

    public void setId(int id) {
        this.id = id;
    }

    public AgentType getType() {
        return type;
    }

    public void setType(AgentType type) {
        this.type = type;
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
}
