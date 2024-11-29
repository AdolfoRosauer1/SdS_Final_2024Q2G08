package itba.edu.ar;

import itba.edu.ar.simulation.Agent;
import itba.edu.ar.simulation.Simulation;
import itba.edu.ar.simulation.SimulationSnapshot;

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

    public static void saveResults(Simulation simulation, String outputDirectory) throws IOException {
        List<SimulationSnapshot> snapshots = simulation.getSnapshots();
        int realizationNumber = simulation.getRealizationNumber();

        // Guardar datos en archivos CSV
        String filename = outputDirectory + "/realization_" + realizationNumber + ".csv";
        FileWriter csvWriter = new FileWriter(filename);

        // Escribir encabezados
        csvWriter.append("Time,AgentID,AgentType,PosX,PosY,Radius\n");

        // Escribir datos de cada snapshot
        for (SimulationSnapshot snapshot : snapshots) {
            double time = snapshot.getTime();
            for (Agent agent : snapshot.getAgents()) {
                csvWriter.append(time + ",");
                csvWriter.append(agent.getId() + ",");
                csvWriter.append(agent.getType().toString() + ",");
                csvWriter.append(agent.getPosition().getX() + ",");
                csvWriter.append(agent.getPosition().getY() + ",");
                csvWriter.append(agent.getRadius() + "\n");
            }
        }

        csvWriter.flush();
        csvWriter.close();
    }
}
