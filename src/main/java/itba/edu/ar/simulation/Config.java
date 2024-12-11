package itba.edu.ar.simulation;

import java.util.List;

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
    private double nH;
    private double nZ;

    // Noise Direction
    private double noiseDir;

    // Simulation Parameters
    private double arenaRadius;
    private double simulationTime;
    private double currentTime = 0.0;
    private List<Double> probabilities;
    private double timeStep;
    private int realizations;
    private String outputDirectory;
    private boolean saveVelocitiesAndPercentages;
    private boolean savePositions;
    private boolean saveFinishStates;
    private boolean orianaAnalysis;
    private double minTimeToCalculateVelocity;

    // Zombies/Humans Parameters
    private List<Integer> initialHumansList;
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

    public Config(double cpmBeta, double az, double bz, double ah, double bh, double aw, double bw, double nH,
            double nZ, double noiseDir, double arenaRadius, double simulationTime, double currentTime,
            List<Double> probabilities, double timeStep, int realizations, String outputDirectory,
            boolean saveVelocitiesAndPercentages, boolean savePositions, boolean saveFinishStates,
            boolean orianaAnalysis, double minTimeToCalculateVelocity, int initialHumans, List<Integer> initialHumansList, int initialZombies, double humanSpeed,
            double zombieSpeed, double contactDuration, double probabilityInfection, double minRadius, double maxRadius,
            double relaxationTime) {
        this.cpmBeta = cpmBeta;
        this.az = az;
        this.bz = bz;
        this.ah = ah;
        this.bh = bh;
        this.aw = aw;
        this.bw = bw;
        this.nH = nH;
        this.nZ = nZ;
        this.noiseDir = noiseDir;
        this.arenaRadius = arenaRadius;
        this.simulationTime = simulationTime;
        this.currentTime = currentTime;
        this.probabilities = probabilities;
        this.timeStep = timeStep;
        this.realizations = realizations;
        this.outputDirectory = outputDirectory;
        this.saveVelocitiesAndPercentages = saveVelocitiesAndPercentages;
        this.savePositions = savePositions;
        this.saveFinishStates = saveFinishStates;
        this.orianaAnalysis = orianaAnalysis;
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
        this.nH = config.nH;
        this.nZ = config.nZ;
        this.noiseDir = config.noiseDir;
        this.arenaRadius = config.arenaRadius;
        this.simulationTime = config.simulationTime;
        this.currentTime = config.currentTime;
        this.probabilities = config.probabilities;
        this.timeStep = config.timeStep;
        this.realizations = config.realizations;
        this.outputDirectory = config.outputDirectory;
        this.saveVelocitiesAndPercentages = config.saveVelocitiesAndPercentages;
        this.savePositions = config.savePositions;
        this.saveFinishStates = config.saveFinishStates;
        this.orianaAnalysis = config.orianaAnalysis;
        this.minTimeToCalculateVelocity = config.minTimeToCalculateVelocity;
        this.initialHumans = config.initialHumans;
        this.initialHumansList = config.initialHumansList;
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

    public double getnH() {
        return nH;
    }

    public void setnH(double nH) {
        this.nH = nH;
    }

    public double getnZ() {
        return nZ;
    }

    public void setnZ(double nZ) {
        this.nZ = nZ;
    }

    public List<Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(List<Double> probabilities) {
        this.probabilities = probabilities;
    }

    public boolean isSaveVelocitiesAndPercentages() {
        return saveVelocitiesAndPercentages;
    }

    public void setSaveVelocitiesAndPercentages(boolean saveVelocitiesAndPercentages) {
        this.saveVelocitiesAndPercentages = saveVelocitiesAndPercentages;
    }

    public boolean isSavePositions() {
        return savePositions;
    }

    public void setSavePositions(boolean savePositions) {
        this.savePositions = savePositions;
    }

    public boolean isSaveFinishStates() {
        return saveFinishStates;
    }

    public void setSaveFinishStates(boolean saveFinishStates) {
        this.saveFinishStates = saveFinishStates;
    }

    public boolean isOrianaAnalysis() {
        return orianaAnalysis;
    }

    public void setOrianaAnalysis(boolean orianaAnalysis) {
        this.orianaAnalysis = orianaAnalysis;
    }

    public List<Integer> getInitialHumansList() {
        return initialHumansList;
    }

    public void setInitialHumansList(List<Integer> initialHumansList) {
        this.initialHumansList = initialHumansList;
    }
}
