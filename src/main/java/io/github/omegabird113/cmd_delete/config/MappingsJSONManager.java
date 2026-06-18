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
                throw new JsonParseException("Builtin mappings id \"" + registry.getId() + "\" does not match filename \"" + id + "\"");
            return registry;
        }
    }

    private static MappingsRegistry loadFromCustomMappingsDir(String id) throws IOException {
        Path path = CmdDeleteClient.MAPPINGS_JSONS_PATH.resolve(id + ".json");
        if (!Files.exists(path))
            throw new FileNotFoundException("Custom mapping file not found at: " + path);

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
        Optional<MappingsRegistry> registry = getRegistryFrom(true, id);
        if (registry.isPresent()) {
            try {
                MappingsRegistry resolved = resolveInheritance(registry.get());
                mappings.setRegistry(resolved);
                return true;
            } catch (IOException e) {
                CmdDeleteClient.LOGGER.error("Failed to resolve custom mappings inheritance for \"{}\"", id, e);
                return false;
            }
        } else
            return false;
    }

    public static boolean tryLoadBuiltinMappings(String id, NavMappings mappings) {
        Optional<MappingsRegistry> registry = getRegistryFrom(false, id);
        if (registry.isPresent()) {
            mappings.setRegistry(registry.get());
            return true;
        } else
            return false;
    }

    public static Optional<MappingsRegistry> getRegistryFrom(boolean custom, String id) {
        try {
            MappingsRegistry registry = custom ? loadFromCustomMappingsDir(id) : loadFromResourceMappingsDir(id);
            return Optional.of(registry);
        } catch (FileNotFoundException _) {
            CmdDeleteClient.LOGGER.error("Could not access {} mapping file \"{}\" because it does not exist.", custom ? "custom" : "builtin", id);
            return Optional.empty();
        } catch (IOException | JsonParseException e) {
            CmdDeleteClient.LOGGER.error("Could not access {} mapping file due to exception: {}", custom ? "custom" : "builtin", id, e);
            return Optional.empty();
        }
    }

    private static MappingsRegistry resolveInheritance(MappingsRegistry startRegistry) throws IOException {
        List<MappingsRegistry> registries = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        MappingsRegistry current = startRegistry;
        while (true) {
            registries.add(current);
            ids.add(current.getId());
            if (current.getInherits().isEmpty())
                break;
            else {
                boolean custom = current.getInherits().startsWith("custom:");
                String idToGet = current.getInherits().replaceFirst("custom:|builtin:", "");
                Optional<MappingsRegistry> newRegistry = getRegistryFrom(custom, idToGet);
                if (newRegistry.isEmpty()) {
                    throw new IOException("Failed to resolve inheritance of " + (custom ? "custom" : "builtin") + " mappings \"" + idToGet + "\" by mappings \"" + current.getId() + "\"");
                }
                if (ids.contains(newRegistry.get().getId())) {
                    throw new IOException("Duplicate inheritance of " + (custom ? "custom" : "builtin") + " mappings \"" + idToGet + "\" by mappings \"" + current.getId() + "\" in chain of: " + registries);
                }
                current = newRegistry.get();
            }
        }
        return MappingsInheritanceManager.merge(registries.reversed());
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
