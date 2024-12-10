package itba.edu.ar;

import itba.edu.ar.simulation.Config;
import itba.edu.ar.simulation.Simulation;
import itba.edu.ar.simulation.FinishState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static void main(String[] args) {
        // Cargar configuración
        Config config = ConfigLoader.load("config/config.json");

        // Crear directorio de salida si no existe
        OutputHandler.createOutputDirectory(config.getOutputDirectory());

        // Notificar las configuraciones
        System.out.println("Configuraciones: " + config.getProbabilities());
        System.out.println("realizaciones: " + config.getRealizations());

        // Ejecutar múltiples realizaciones
        int realizations = config.getRealizations();
        List<Double> probabilities = config.getProbabilities();
        List<FinishState> finishStates = Collections.synchronizedList(new ArrayList<>());

        long startTime = System.currentTimeMillis();
        int totalSimulations = probabilities.size() * realizations;
        AtomicInteger completedSimulations = new AtomicInteger(0);

        // Create thread pool
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        CountDownLatch latch = new CountDownLatch(totalSimulations);

        for (Double probability : probabilities) {
            config.setProbabilityInfection(probability);

            // Submit all realizations for this probability
            for (int realization = 1; realization <= realizations; realization++) {
                final int currentRealization = realization;
                executor.submit(() -> {
                    try {
                        // Inicializar simulación
                        Config configCopy = new Config(config);
                        configCopy.setProbabilityInfection(probability);
                        Simulation simulation = new Simulation(configCopy, currentRealization);

                        // Ejecutar simulación
                        FinishState finishState = simulation.run();
                        finishStates.add(finishState);

                        // Guardar resultados
                        if (config.isSavePositions()) {
                            try {
                                OutputHandler.savePositions(config.getOutputDirectory(), simulation);
                            } catch (IOException e) {
                                System.err.println(
                                        "Error al guardar los resultados: " + e.getMessage());
                            }
                        }

                        if (config.isSaveVelocitiesAndPercentages()) {
                            try {
                                OutputHandler.saveVelocitiesAndPercentages(config.getOutputDirectory(),
                                        simulation);
                            } catch (IOException e) {
                                System.err.println(
                                        "Error al guardar los resultados: " + e.getMessage());
                            }
                        }

                        // Update progress
                        int completed = completedSimulations.incrementAndGet();
                        int progressBarWidth = 50;
                        int progress = (int) ((double) completed / totalSimulations * progressBarWidth);

                        // Calculate ETA
                        long currentTime = System.currentTimeMillis();
                        long elapsedTime = currentTime - startTime;
                        long estimatedTotalTime = (long) ((double) elapsedTime / completed
                                * totalSimulations);
                        long remainingTime = estimatedTotalTime - elapsedTime;
                        long remainingMinutes = remainingTime / (1000 * 60);
                        long remainingSeconds = (remainingTime / 1000) % 60;

                        // Print progress bar
                        StringBuilder progressBar = new StringBuilder("\r[");
                        for (int i = 0; i < progressBarWidth; i++) {
                            if (i < progress) {
                                progressBar.append("█");
                            } else {
                                progressBar.append(" ");
                            }
                        }
                        progressBar.append(String.format("] %d%% (P=%.2f, R=%d/%d) ETA: %dm %ds",
                                (int) ((double) completed / totalSimulations * 100),
                                probability,
                                currentRealization,
                                realizations,
                                remainingMinutes,
                                remainingSeconds));

                        synchronized (System.out) {
                            System.out.print(progressBar);
                        }

                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        // Wait for all realizations to complete
        try {
            latch.await();
            if (config.isSaveFinishStates()) {
                try {
                    OutputHandler.saveFinishStates(finishStates, config);
                } catch (IOException e) {
                    System.err.println("Error al guardar los estados finales: " + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for simulations to complete");
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("\nTodas las realizaciones han finalizado en "
                + (System.currentTimeMillis() - startTime) / 1000 + " segundos.");
    }
}
