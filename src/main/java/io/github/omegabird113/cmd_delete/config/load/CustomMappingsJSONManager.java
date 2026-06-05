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
import java.util.Objects;

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
            return gson.fromJson(reader, CustomMappingsRegistry.class);
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

    public static ArrayList<String> getAvailableOptions() {
        ArrayList<String> options = new ArrayList<>();
        for (File file : Objects.requireNonNull(configPath.toFile().listFiles())) {
            options.add("custom:" + FilenameUtils.removeExtension(file.getName()));
        }
        return options;
    }
}
