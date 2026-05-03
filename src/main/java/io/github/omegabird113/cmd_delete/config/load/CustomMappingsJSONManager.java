package io.github.omegabird113.cmd_delete.config.load;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.mappings.INavMappings;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public static INavMappings tryLoadCustomMappingsElse(String id, INavMappings customMappings, INavMappings fallback) {
        try {
            CustomMappingsRegistry registry = loadFromCustomMappingsDir(id);
            CustomMappingsRegistry.setCurrent(registry);
            return customMappings;
        } catch (IOException e) {
            return fallback;
        }
    }
}
