package itba.edu.ar.simulation;

import java.util.Random;

public class Agent {
    private int id;
    private AgentType type;
    private Vector2D position;
    private Vector2D velocity;
    private double speed;
    private double radius;
    private boolean isInContact;
    private double contactStartTime;
    private Agent contactAgent;

    private Random random = new Random();

    public Agent(int id, AgentType type, double speed, double arenaRadius) {
        this.type = type;
        this.speed = speed;
        this.radius = 0.35; // rmax
        this.isInContact = false;
        this.contactStartTime = 0.0;
        this.contactAgent = null;

        // Inicializar posición aleatoria dentro del área permitida
        this.position = PositionInitializer.initializePosition(arenaRadius);
        this.velocity = new Vector2D(0, 0);
    }

    public Agent( Agent agent ){
        this.id = agent.id;
        this.type = agent.type;
        this.speed = agent.speed;
        this.radius = agent.radius;
        this.isInContact = agent.isInContact;
        this.contactStartTime = agent.contactStartTime;
        this.contactAgent = agent.contactAgent;
        this.position = agent.position;
        this.velocity = agent.velocity;
    }

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
