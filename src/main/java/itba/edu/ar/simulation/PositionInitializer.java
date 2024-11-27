package itba.edu.ar.simulation;

import java.util.Random;

public class PositionInitializer {
    private static Random random = new Random();

    public static Vector2D initializePosition(double arenaRadius) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius = random.nextDouble() * (arenaRadius - 1.0) + 1.0; // Zona de exclusión de 1 m
        double x = radius * Math.cos(angle);
        double y = radius * Math.sin(angle);
        return new Vector2D(x, y);
    }
}
