package itba.edu.ar;

import itba.edu.ar.simulation.Config;
import itba.edu.ar.simulation.Simulation;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        // Cargar configuración
        Config config = ConfigLoader.load("config/config.json");

        // Crear directorio de salida si no existe
        OutputHandler.createOutputDirectory(config.getOutputDirectory());

        // Ejecutar múltiples realizaciones
        int realizations = config.getRealizations();
        for (int realization = 1; realization <= realizations; realization++) {
            // Inicializar simulación
            Simulation simulation = new Simulation(config, realization);

            // Ejecutar simulación
            simulation.run();

            // Guardar resultados
            try {
                OutputHandler.saveResults(simulation, config.getOutputDirectory());
            } catch (IOException e) {
                System.err.println("Error al guardar los resultados: " + e.getMessage());
            }

            // Mostrar progreso
            System.out.printf("Realización %d de %d completada.\n", realization, realizations);
        }

        System.out.println("Todas las realizaciones han finalizado.");
    }
}
