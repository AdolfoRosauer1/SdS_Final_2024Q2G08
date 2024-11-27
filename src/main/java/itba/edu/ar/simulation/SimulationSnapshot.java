package itba.edu.ar.simulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationSnapshot {
    private double time;
    private List<Agent> agents;

    public SimulationSnapshot(double time, List<Agent> agents) {
        this.time = time;
        this.agents = new ArrayList<>();

        // Crear una copia de los agentes para evitar referencias
        for (Agent agent : agents) {
            this.agents.add(new Agent(agent));
        }
    }

    public double getTime() {
        return time;
    }

    public List<Agent> getAgents() {
        return agents;
    }
}
