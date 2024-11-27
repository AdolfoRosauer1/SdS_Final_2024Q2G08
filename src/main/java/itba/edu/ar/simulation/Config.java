package itba.edu.ar.simulation;

public class Config {
    private double arenaRadius;
    private int initialHumans;
    private int initialZombies;
    private double humanSpeed;
    private double zombieSpeed;
    private double contactDuration;
    private double probabilityInfection;
    private double simulationTime;
    private double timeStep;
    private int realizations;
    private String outputDirectory;

    public Config(){}

    public double getArenaRadius() {
        return arenaRadius;
    }

    public void setArenaRadius(double arenaRadius) {
        this.arenaRadius = arenaRadius;
    }

    public int getInitialHumans() {
        return initialHumans;
    }

    public void setInitialHumans(int initialHumans) {
        this.initialHumans = initialHumans;
    }

    public int getInitialZombies() {
        return initialZombies;
    }

    public void setInitialZombies(int initialZombies) {
        this.initialZombies = initialZombies;
    }

    public double getHumanSpeed() {
        return humanSpeed;
    }

    public void setHumanSpeed(double humanSpeed) {
        this.humanSpeed = humanSpeed;
    }

    public double getZombieSpeed() {
        return zombieSpeed;
    }

    public void setZombieSpeed(double zombieSpeed) {
        this.zombieSpeed = zombieSpeed;
    }

    public double getContactDuration() {
        return contactDuration;
    }

    public void setContactDuration(double contactDuration) {
        this.contactDuration = contactDuration;
    }

    public double getProbabilityInfection() {
        return probabilityInfection;
    }

    public void setProbabilityInfection(double probabilityInfection) {
        this.probabilityInfection = probabilityInfection;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public int getRealizations() {
        return realizations;
    }

    public void setRealizations(int realizations) {
        this.realizations = realizations;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
