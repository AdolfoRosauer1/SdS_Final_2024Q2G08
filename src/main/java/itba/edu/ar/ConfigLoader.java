package itba.edu.ar;

import java.io.FileReader;
import com.google.gson.Gson;
import itba.edu.ar.simulation.Config;

public class ConfigLoader {
    public static Config load(String filePath) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(filePath);
            Config config = gson.fromJson(reader, Config.class);
            reader.close();
            return config;
        } catch (Exception e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
            return null;
        }
    }
}
