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

    public double averageVelocity() {
        // Promedio de los vectores de velocidad de todos los agentes
        Vector2D totalVelocity = new Vector2D(0, 0);
        for (Agent agent : agents) {
            totalVelocity = totalVelocity.add(agent.getVelocity());
        }
//        <-- + --> = 0.0/2
        return totalVelocity.divide(agents.size()).magnitude();
    }

    public double zombiePercentage() {
        return (double) getAmountZombies() / (getAmountZombies() + getAmountHumans());
    }

    public int getAmountZombies() {
        int count = 0;
        for (Agent agent : agents) {
            if (agent.getType() == AgentType.ZOMBIE) {
                count++;
            }
        }
        return count;
    }

    public int getAmountHumans() {
        int count = 0;
        for (Agent agent : agents) {
            if (agent.getType() == AgentType.HUMAN) {
                count++;
            }
        }
        return count;
    }

    public double getTime() {
        return time;
    }

    public List<Agent> getAgents() {
        return agents;
    }
}
