package itba.edu.ar;

import itba.edu.ar.simulation.Config;
import itba.edu.ar.simulation.Simulation;
import itba.edu.ar.simulation.FinishState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Cargar configuración
        Config config = ConfigLoader.load("config/config.json");

        // Crear directorio de salida si no existe
        OutputHandler.createOutputDirectory(config.getOutputDirectory());

        // Ejecutar múltiples realizaciones
        int realizations = config.getRealizations();
        List<FinishState> finishStates = new ArrayList<>();
        for (int realization = 1; realization <= realizations; realization++) {
            // Inicializar simulación
            Config configCopy = new Config(config);
            Simulation simulation = new Simulation(configCopy, realization);

            // Ejecutar simulación
            FinishState finishState = simulation.run();
            finishStates.add(finishState);

            // Guardar resultados
            if (config.isSaveSnapshots()) {
                try {
                    OutputHandler.saveResults(simulation, config.getOutputDirectory());
                } catch (IOException e) {
                    System.err.println("Error al guardar los resultados: " + e.getMessage());
                }
            }

            // Mostrar progreso
            System.out.printf("Realización %d de %d completada.\n", realization, realizations);
        }

        try {
            OutputHandler.saveFinishStates(finishStates, config.getOutputDirectory(), config.getProbabilityInfection());
        } catch (IOException e) {
            System.err.println("Error al guardar los estados finales: " + e.getMessage());
        }

        System.out.println("Todas las realizaciones han finalizado.");
    }
}
