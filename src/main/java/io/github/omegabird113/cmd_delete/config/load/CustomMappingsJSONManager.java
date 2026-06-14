package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.mappings.CustomNavMappings;
import io.github.omegabird113.cmd_delete.mappings.INavMappings;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CustomMappingsJSONManager {
    private static final Path gamePath = FabricLoader.getInstance().getGameDir();
    private static final Path configPath = gamePath.resolve("config/cmd_delete/mappings/");

    private static CustomMappingsRegistry loadFromCustomMappingsDir(String id) throws IOException {
        Path path = configPath.resolve(id + ".json");
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Custom mapping file not found at: " + path);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CustomMappingsRegistry.class, new CustomMappingsJSONDeserializer())
                .create();

        try (java.io.BufferedReader reader = Files.newBufferedReader(path)) {
            CustomMappingsRegistry registry = gson.fromJson(reader, CustomMappingsRegistry.class);
            registry.setFilename(id);
            return registry;
        }
    }

    public static INavMappings tryLoadCustomMappingsElse(String id, CustomNavMappings customMappings, INavMappings fallback) {
        if (tryLoadCustomMappings(id, customMappings)) {
            return customMappings;
        }
        return fallback;
    }

    public static boolean tryLoadCustomMappings(String id, CustomNavMappings customMappings) {
        try {
            CustomMappingsRegistry registry = loadFromCustomMappingsDir(id);
            customMappings.setRegistry(registry);
            return true;
        } catch (IOException e) {
            CmdDeleteClient.LOGGER.error("Could not load custom mapping file: {}", id, e);
            return false;
        }
    }

    public static void tryMakeConfigFiles() {
        File configDirectory = configPath.toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            boolean s = configDirectory.mkdirs();
            if (!s) {
                CmdDeleteClient.LOGGER.error("Could not create mappings config directory at: {}", configDirectory);
            }
            else
                CmdDeleteClient.LOGGER.info("Created mappings config directory at: {}", configDirectory.getAbsolutePath());
        }

        File activeMappingsFile = gamePath.resolve("config/cmd_delete/.active_mappings").toFile();
        if (!activeMappingsFile.exists() || !activeMappingsFile.isFile()) {
            try {
                Files.createDirectories(activeMappingsFile.toPath().getParent());
                boolean s = activeMappingsFile.createNewFile();
                if (!s) {
                    CmdDeleteClient.LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath());
                }
                else
                    CmdDeleteClient.LOGGER.info("Created active mappings file at: {}", activeMappingsFile.getAbsolutePath());
            } catch (IOException e) {
                CmdDeleteClient.LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath(), e);
            }
        }
    }

    public static ArrayList<String> getAvailableOptions() {
        ArrayList<String> options = new ArrayList<>();

        File configDirectory = configPath.toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            tryMakeConfigFiles();
            return options;
        }

        File[] files = configDirectory.listFiles();
        if (files == null)
            return options;

        for (File file : files)
            if (file.getName().endsWith(".json"))
                options.add("custom:" + FilenameUtils.removeExtension(file.getName()));

        return options;
    }
}
