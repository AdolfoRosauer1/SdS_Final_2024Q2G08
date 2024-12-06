package itba.edu.ar.simulation;

public class Config {
    // CPM Parameters
    private double cpmBeta;

    // Avoidance
    private double az;
    private double bz;
    private double ah;
    private double bh;
    private double aw;
    private double bw;

    // Noise Direction
    private double noiseDir;


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

    public Config() {
    }

    public Config(double cpmBeta, double az, double bz, double ah, double bh, double aw, double bw, double noiseDir, double arenaRadius, double simulationTime, double currentTime, double timeStep, int realizations, String outputDirectory, boolean saveSnapshots, double minTimeToCalculateVelocity, int initialHumans, int initialZombies, double humanSpeed, double zombieSpeed, double contactDuration, double probabilityInfection, double minRadius, double maxRadius, double relaxationTime) {
        this.cpmBeta = cpmBeta;
        this.az = az;
        this.bz = bz;
        this.ah = ah;
        this.bh = bh;
        this.aw = aw;
        this.bw = bw;
        this.noiseDir = noiseDir;
        this.arenaRadius = arenaRadius;
        this.simulationTime = simulationTime;
        this.currentTime = currentTime;
        this.timeStep = timeStep;
        this.realizations = realizations;
        this.outputDirectory = outputDirectory;
        this.saveSnapshots = saveSnapshots;
        this.minTimeToCalculateVelocity = minTimeToCalculateVelocity;
        this.initialHumans = initialHumans;
        this.initialZombies = initialZombies;
        this.humanSpeed = humanSpeed;
        this.zombieSpeed = zombieSpeed;
        this.contactDuration = contactDuration;
        this.probabilityInfection = probabilityInfection;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.relaxationTime = relaxationTime;
    }

    public Config(Config config) {
        this.cpmBeta = config.cpmBeta;
        this.az = config.az;
        this.bz = config.bz;
        this.ah = config.ah;
        this.bh = config.bh;
        this.aw = config.aw;
        this.bw = config.bw;
        this.noiseDir = config.noiseDir;
        this.arenaRadius = config.arenaRadius;
        this.simulationTime = config.simulationTime;
        this.currentTime = config.currentTime;
        this.timeStep = config.timeStep;
        this.realizations = config.realizations;
        this.outputDirectory = config.outputDirectory;
        this.saveSnapshots = config.saveSnapshots;
        this.minTimeToCalculateVelocity = config.minTimeToCalculateVelocity;
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


    public double getCpmBeta() {
        return cpmBeta;
    }

    public void setCpmBeta(double cpmBeta) {
        this.cpmBeta = cpmBeta;
    }

    public double getAz() {
        return az;
    }

    public void setAz(double az) {
        this.az = az;
    }

    public double getBz() {
        return bz;
    }

    public void setBz(double bz) {
        this.bz = bz;
    }

    public double getAh() {
        return ah;
    }

    public void setAh(double ah) {
        this.ah = ah;
    }

    public double getBh() {
        return bh;
    }

    public void setBh(double bh) {
        this.bh = bh;
    }

    public double getAw() {
        return aw;
    }

    public void setAw(double aw) {
        this.aw = aw;
    }

    public double getBw() {
        return bw;
    }

    public void setBw(double bw) {
        this.bw = bw;
    }

    public double getNoiseDir() {
        return noiseDir;
    }

    public void setNoiseDir(double noiseDir) {
        this.noiseDir = noiseDir;
    }

    public double getArenaRadius() {
        return arenaRadius;
    }

    public void setArenaRadius(double arenaRadius) {
        this.arenaRadius = arenaRadius;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
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

    public double getMinTimeToCalculateVelocity() {
        return minTimeToCalculateVelocity;
    }

    public void setMinTimeToCalculateVelocity(double minTimeToCalculateVelocity) {
        this.minTimeToCalculateVelocity = minTimeToCalculateVelocity;
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
}
