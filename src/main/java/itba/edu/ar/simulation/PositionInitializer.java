package itba.edu.ar.simulation;

import java.util.List;
import java.util.Random;

public class PositionInitializer {
    private static Random random = new Random();

    public static Vector2D initializePosition(Config config, List<Agent> agents, Agent toPosition) {
        int maxAttempts = 100;
        int attempts = 0;

        if (config.getInitialZombies() == 1 && toPosition.getType() == AgentType.ZOMBIE) {
            return new Vector2D(0, 0);
        }

        while (attempts < maxAttempts) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius;

            // For zombies when there are multiple zombies, try to place in inner third
            // first
            if (config.getInitialZombies() > 1 && toPosition.getType() == AgentType.ZOMBIE) {
                radius = random.nextDouble() * (config.getArenaRadius() / 3.0);
            } else {
                radius = random.nextDouble() * (config.getArenaRadius() - 1.0) + 1.0;
            }

            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            Vector2D position = new Vector2D(x, y);

            boolean validPosition = true;
            for (Agent agent : agents) {
                Vector2D diff = position.subtract(agent.getPosition());
                double distance = diff.magnitude();
                // Check if position overlaps with any existing agent
                if (distance < agent.getRadius() * 2) {
                    validPosition = false;
                    break;
                }
            }

            if (validPosition) {
                return position;
            }

            attempts++;
        }

        // If no valid position found after max attempts, try placing in random position
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius = random.nextDouble() * (config.getArenaRadius() - 1.0) + 1.0;
        return new Vector2D(radius * Math.cos(angle), radius * Math.sin(angle));
    }
}
