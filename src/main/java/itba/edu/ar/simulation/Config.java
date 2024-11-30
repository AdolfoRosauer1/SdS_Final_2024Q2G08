package itba.edu.ar.simulation;

public class Config {
    // CPM Parameters
    private double cpmBeta;

    // Social Force Parameters
    private double socialForceWeight;
    private double escapeForceWeight;
    private double boundaryForceWeight;
    private double socialForceRadius;
    private double zombieDetectionRadius;

    // Simulation Parameters
    private double arenaRadius;
    private double simulationTime;
    private double currentTime = 0.0;
    private double timeStep;
    private int realizations;
    private String outputDirectory;
    private boolean saveSnapshots;
    private double minTimeToCalculateVelocity;

    // Zombies/Humans Parameters
    private int initialHumans;
    private int initialZombies;
    private double humanSpeed;
    private double zombieSpeed;
    private double contactDuration;
    private double probabilityInfection;

    // Physical Properties
    private double minRadius;
    private double maxRadius;
    private double relaxationTime;

    public Config(){}

    public Config(Config config) {
        this.cpmBeta = config.cpmBeta;
        this.socialForceWeight = config.socialForceWeight;
        this.escapeForceWeight = config.escapeForceWeight;
        this.boundaryForceWeight = config.boundaryForceWeight;
        this.socialForceRadius = config.socialForceRadius;
        this.zombieDetectionRadius = config.zombieDetectionRadius;
        this.arenaRadius = config.arenaRadius;
        this.simulationTime = config.simulationTime;
        this.currentTime = config.currentTime;
        this.timeStep = config.timeStep;
        this.realizations = config.realizations;
        this.outputDirectory = config.outputDirectory;
        this.saveSnapshots = config.saveSnapshots;
        this.initialHumans = config.initialHumans;
        this.initialZombies = config.initialZombies;
        this.humanSpeed = config.humanSpeed;
        this.zombieSpeed = config.zombieSpeed;
        this.contactDuration = config.contactDuration;
        this.probabilityInfection = config.probabilityInfection;
        this.minRadius = config.minRadius;
        this.maxRadius = config.maxRadius;
        this.relaxationTime = config.relaxationTime;
    }

    public double getMinTimeToCalculateVelocity() {
        return minTimeToCalculateVelocity;
    }

    public void setMinTimeToCalculateVelocity(double minTimeToCalculateVelocity) {
        this.minTimeToCalculateVelocity = minTimeToCalculateVelocity;
    }

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


    public boolean isSaveSnapshots() {
        return saveSnapshots;
    }


    public void setSaveSnapshots(boolean saveSnapshots) {
        this.saveSnapshots = saveSnapshots;
    }


    public double getMinRadius() {
        return minRadius;
    }


    public void setMinRadius(double minRadius) {
        this.minRadius = minRadius;
    }


    public double getMaxRadius() {
        return maxRadius;
    }


    public void setMaxRadius(double maxRadius) {
        this.maxRadius = maxRadius;
    }


    public double getRelaxationTime() {
        return relaxationTime;
    }


    public void setRelaxationTime(double relaxationTime) {
        this.relaxationTime = relaxationTime;
    }


    public double getCpmBeta() {
        return cpmBeta;
    }


    public void setCpmBeta(double cpmBeta) {
        this.cpmBeta = cpmBeta;
    }


    public double getCurrentTime() {
        return currentTime;
    }


    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }


    public double getSocialForceWeight() {
        return socialForceWeight;
    }


    public void setSocialForceWeight(double socialForceWeight) {
        this.socialForceWeight = socialForceWeight;
    }


    public double getEscapeForceWeight() {
        return escapeForceWeight;
    }


    public void setEscapeForceWeight(double escapeForceWeight) {
        this.escapeForceWeight = escapeForceWeight;
    }


    public double getBoundaryForceWeight() {
        return boundaryForceWeight;
    }


    public void setBoundaryForceWeight(double boundaryForceWeight) {
        this.boundaryForceWeight = boundaryForceWeight;
    }


    public double getSocialForceRadius() {
        return socialForceRadius;
    }


    public void setSocialForceRadius(double socialForceRadius) {
        this.socialForceRadius = socialForceRadius;
    }


    public double getZombieDetectionRadius() {
        return zombieDetectionRadius;
    }


    public void setZombieDetectionRadius(double zombieDetectionRadius) {
        this.zombieDetectionRadius = zombieDetectionRadius;
    }
}
