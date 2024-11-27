package itba.edu.ar.simulation;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Config config;
    private int realizationNumber;
    private List<Agent> agents;
    private double currentTime;
    private List<SimulationSnapshot> snapshots;


    public Simulation(Config config, int realizationNumber) {
        this.config = config;
        this.realizationNumber = realizationNumber;
        this.agents = new ArrayList<>();
        this.snapshots = new ArrayList<>();
        this.currentTime = 0.0;
        initializeAgents();
    }

    private void initializeAgents() {
        // Inicializar zombies
        int i;
        for (i = 0; i < config.getInitialZombies(); i++) {
            Agent zombie = new Agent(i, AgentType.ZOMBIE, config.getZombieSpeed(), config.getArenaRadius(), config.getMinRadius(), config.getMaxRadius(), config.getRelaxationTime(), config.getCpmBeta());
            agents.add(zombie);
        }

        // Inicializar humanos
        for (int j = 0; j < config.getInitialHumans(); j++) {
            Agent human = new Agent(i+j, AgentType.HUMAN, config.getHumanSpeed(), config.getArenaRadius(), config.getMinRadius(), config.getMaxRadius(), config.getRelaxationTime(), config.getCpmBeta());
            agents.add(human);
        }
    }

    public void run() {
        while (currentTime < config.getSimulationTime()) {
            // Actualizar estado de los agentes
            updateAgents();

            if (config.isSaveSnapshots()) {
                // Guardar snapshot para análisis
                saveSnapshot();
            }

            // Incrementar tiempo
            currentTime += config.getTimeStep();
        }
    }

    private void updateAgents() {
        // TODO: Implementar la lógica de actualización de los agentes
    }

    private void saveSnapshot() {
        SimulationSnapshot snapshot = new SimulationSnapshot(currentTime, agents);
        snapshots.add(snapshot);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public int getRealizationNumber() {
        return realizationNumber;
    }

    public void setRealizationNumber(int realizationNumber) {
        this.realizationNumber = realizationNumber;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<SimulationSnapshot> snapshots) {
        this.snapshots = snapshots;
    }
}
