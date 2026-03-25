package net.bl4st.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

public class ModConfig {
    public static float ELYTRA_DRAG = 2.0F;

    public static float MAXIMUM_FALLDISTANCE = 15.0F;

    public static float MINIMUM_SPEED = 0.1F;

    public static void LoadConfig() {
        String configPath = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), new String[] { "elytradrag.properties" }).toString();
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream(configPath);
            try {
                properties.load(input);
                ELYTRA_DRAG = Float.parseFloat(properties.getProperty("drag", "2.0"));
                MAXIMUM_FALLDISTANCE = Float.parseFloat(properties.getProperty("maximum-fall-distance-while-slowing-Down", "15.0"));
                MINIMUM_SPEED = Float.parseFloat(properties.getProperty("minimum-speed-required", "0.1"));
                input.close();
            } catch (Throwable throwable) {
                try {
                    input.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (FileNotFoundException e) {
            try {
                OutputStream output = new FileOutputStream(configPath);
                try {
                    properties.setProperty("drag", "2.0");
                    properties.setProperty("maximum-fall-distance-while-slowing-Down", "15.0");
                    properties.setProperty("minimum-speed-required", "0.1");
                    properties.store(output, "Elytra Drag Configuration");
                    output.close();
                } catch (Throwable throwable) {
                    try {
                        output.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            } catch (IOException ex) {
                System.err.println("Error creating default config file: " + ex.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing config values: " + e.getMessage());
        }
    }
}
