package io.github.omegabird113.cmd_delete.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MappingsJSONManager {
    private MappingsJSONManager() {
    }

    private static MappingsRegistry loadFromResourceMappingsDir(String id) throws IOException {
        Optional<ModContainer> mod = FabricLoader.getInstance()
                .getModContainer(CmdDeleteClient.MODID);

        Path path = mod.orElseThrow()
                .findPath("mappings/" + id + ".json")
                .orElseThrow(() -> new FileNotFoundException(id));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MappingsRegistry.class, new MappingsJSONDeserializer())
                .create();

        try (java.io.BufferedReader reader = Files.newBufferedReader(path)) {
            MappingsRegistry registry = gson.fromJson(reader, MappingsRegistry.class);
            if (!registry.getId().equals(id))
                throw new JsonParseException("Custom mappings id \"" + registry.getId() + "\" does not match filename \"" + id + "\"");
            return registry;
        }
    }

    private static MappingsRegistry loadFromCustomMappingsDir(String id) throws IOException {
        Path path = CmdDeleteClient.MAPPINGS_JSONS_PATH.resolve(id + ".json");
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Custom mapping file not found at: " + path);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MappingsRegistry.class, new MappingsJSONDeserializer())
                .create();

        try (java.io.BufferedReader reader = Files.newBufferedReader(path)) {
            MappingsRegistry registry = gson.fromJson(reader, MappingsRegistry.class);
            if (!registry.getId().equals(id))
                throw new JsonParseException("Custom mappings id \"" + registry.getId() + "\" does not match filename \"" + id + "\"");
            return registry;
        }
    }

    public static boolean tryLoadCustomMappings(String id, NavMappings mappings) {
        try {
            MappingsRegistry registry = loadFromCustomMappingsDir(id);
            mappings.setRegistry(registry);
            return true;
        } catch (FileNotFoundException _) {
            CmdDeleteClient.LOGGER.error("Could not load custom mapping file \"{}\" because it does not exist.", id);
            return false;
        } catch (IOException | JsonParseException e) {
            CmdDeleteClient.LOGGER.error("Could not load custom mapping file due to exception: {}", id, e);
            return false;
        }
    }

    public static boolean tryLoadBuiltinMappings(String id, NavMappings mappings) {
        try {
            MappingsRegistry registry = loadFromResourceMappingsDir(id);
            mappings.setRegistry(registry);
            return true;
        } catch (FileNotFoundException _) {
            CmdDeleteClient.LOGGER.error("Could not load builtin mapping file \"{}\" because it does not exist.", id);
            return false;
        } catch (IOException | JsonParseException e) {
            CmdDeleteClient.LOGGER.error("Could not load builtin mapping file due to exception: {}", id, e);
            return false;
        }
    }

    public static void tryMakeConfigFiles() {
        File configDirectory = CmdDeleteClient.MAPPINGS_JSONS_PATH.toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            boolean s = configDirectory.mkdirs();
            if (!s)
                CmdDeleteClient.LOGGER.error("Could not create mappings config directory at: {}", configDirectory);
            else
                CmdDeleteClient.LOGGER.info("Created mappings config directory at: {}", configDirectory.getAbsolutePath());
        }
        File activeMappingsFile = CmdDeleteClient.ACTIVE_MAPPINGS_FILE_PATH.toFile();
        if (!activeMappingsFile.exists() || !activeMappingsFile.isFile()) {
            try {
                boolean s = activeMappingsFile.createNewFile();
                if (!s)
                    CmdDeleteClient.LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath());
                else
                    CmdDeleteClient.LOGGER.info("Created active mappings file at: {}", activeMappingsFile.getAbsolutePath());
            } catch (IOException e) {
                CmdDeleteClient.LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath(), e);
            }
        }
    }

    public static List<String> getAvailableOptions() {
        List<String> options = new ArrayList<>();

        File configDirectory = CmdDeleteClient.MAPPINGS_JSONS_PATH.toFile();
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
