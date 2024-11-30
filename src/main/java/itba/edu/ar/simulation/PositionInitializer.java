package itba.edu.ar.simulation;

import java.util.List;
import java.util.Random;

public class PositionInitializer {
    private static Random random = new Random();

    public static Vector2D initializePosition(double arenaRadius, List<Agent> agents) {
        int maxAttempts = 100;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = random.nextDouble() * (arenaRadius - 1.0) + 1.0;
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
        
        // If no valid position found after max attempts, try placing further out
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius = arenaRadius * 0.9; // Place near edge as last resort
        return new Vector2D(radius * Math.cos(angle), radius * Math.sin(angle));
    }
}
