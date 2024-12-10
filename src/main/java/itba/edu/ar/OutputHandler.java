package itba.edu.ar;

import itba.edu.ar.simulation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputHandler {
    public static void createOutputDirectory(String outputDirectory) {
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void saveFinishStates(List<FinishState> finishStates, Config config) throws IOException {
        String filename = config.getOutputDirectory() + "/finish_states_" + config.getProbabilityInfection() + ".csv";
        FileWriter csvWriter = new FileWriter(filename);

        csvWriter.append("Id,Time,NumZombies,NumHumans,averageVelocity\n");
        int id = 1;
        for (FinishState finishState : finishStates) {
            if (finishState.time() < config.getMinTimeToCalculateVelocity()) {
                continue;
            }
            String toWrite = id++ + "," + finishState.time() + "," + finishState.num_zombies() + ","
                    + finishState.num_humans() + "," + finishState.averageVelocity() + "\n";
            csvWriter.append(toWrite);
        }

        csvWriter.flush();
        csvWriter.close();
    }

    public static void saveVelocitiesAndPercentages(String outputDirectory, Simulation simulation) throws IOException {
        List<SimulationSnapshot> snapshots = simulation.getSnapshots();
        String velFilename = outputDirectory + "/realization_" + simulation.getConfig().getProbabilityInfection() + "_"
                + simulation.getRealizationNumber() + "_vel.csv";
        FileWriter velCsvWriter = new FileWriter(velFilename);

        // Write header
        velCsvWriter.append("Time,zombiePercentage,averageVelocity\n");

        // Write data from each snapshot
        for (SimulationSnapshot snapshot : snapshots) {
            velCsvWriter.append(snapshot.getTime() + ",");
            velCsvWriter.append(snapshot.zombiePercentage() + ",");
            velCsvWriter.append(snapshot.averageVelocity() + "\n");
        }

        velCsvWriter.flush();
        velCsvWriter.close();
    }

    public static void savePositions(String outputDirectory, Simulation simulation) throws IOException {
        List<SimulationSnapshot> snapshots = simulation.getSnapshots();
        String posFilename = outputDirectory + "/realization_" + simulation.getConfig().getProbabilityInfection() + "_"
                + simulation.getRealizationNumber() + ".csv";
        FileWriter posCsvWriter = new FileWriter(posFilename);

        // Write header
        posCsvWriter.append("Time,AgentID,AgentType,PosX,PosY,Radius\n");

        // Write data from each snapshot
        for (SimulationSnapshot snapshot : snapshots) {
            double time = snapshot.getTime();
            for (Agent agent : snapshot.getAgents()) {
                posCsvWriter.append(time + ",");
                posCsvWriter.append(agent.getId() + ",");
                posCsvWriter.append(agent.getType().toString() + ",");
                posCsvWriter.append(agent.getPosition().getX() + ",");
                posCsvWriter.append(agent.getPosition().getY() + ",");
                posCsvWriter.append(agent.getRadius() + "\n");
            }
        }

        posCsvWriter.flush();
        posCsvWriter.close();
    }

    public static void saveResults(Simulation simulation, String outputDirectory) throws IOException {
        // Save velocities and percentages
        if (simulation.getConfig().isSaveVelocitiesAndPercentages()) {
            saveVelocitiesAndPercentages(outputDirectory, simulation);
        }

        // Save positions
        if (simulation.getConfig().isSavePositions()) {
            savePositions(outputDirectory, simulation);
        }
    }
}
