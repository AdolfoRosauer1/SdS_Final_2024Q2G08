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
            Agent zombie = new Agent(i, AgentType.ZOMBIE, agents, config);
            agents.add(zombie);
        }

        // Inicializar humanos
        for (int j = 0; j < config.getInitialHumans(); j++) {
            Agent human = new Agent(i+j, AgentType.HUMAN, agents, config);
            agents.add(human);
        }
    }

    public FinishState run() {
        while (currentTime < config.getSimulationTime()) {
            // Actualizar estado de los agentes
            updateAgents();

            if (config.isSaveSnapshots()) {
                // Guardar snapshot para análisis
                saveSnapshot();
            }

            // Incrementar tiempo
            currentTime += config.getTimeStep();
            config.setCurrentTime(currentTime);
            if (getAmountHumans() == 0 || getAmountZombies() == 0) {
                break;
            }
        }
        return new FinishState(currentTime, getAmountZombies(), getAmountHumans(), averageVelocity());
    }

    public double averageVelocity(){
        // Promedio de los vectores de velocidad de todos los agentes
        Vector2D totalVelocity = new Vector2D(0, 0);
        for (Agent agent : agents) {
            totalVelocity = totalVelocity.add(agent.getVelocity());
        }
//        <-- + --> = 0.0/2
        return totalVelocity.divide(agents.size()).magnitude();
    }

    public int getAmountZombies(){
        int count = 0;
        for (Agent agent : agents) {
            if (agent.getType() == AgentType.ZOMBIE) {
                count++;
            }
        }
        return count;
    }

    public int getAmountHumans(){
        int count = 0;
        for (Agent agent : agents) {
            if (agent.getType() == AgentType.HUMAN) {
                count++;
            }
        }
        return count;
    }

    private void updateAgents() {
        // Mover a los agentes
        for (Agent agent : agents) {
            agent.updatePosition(config.getTimeStep());
        }

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
