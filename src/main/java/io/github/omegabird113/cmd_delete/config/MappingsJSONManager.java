package io.github.omegabird113.cmd_delete.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import org.apache.commons.io.FilenameUtils;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MappingsJSONManager {
    private static final Logger LOGGER = LoggingManager.getLogger(MappingsJSONManager.class);

    private MappingsJSONManager() {
    }

    private static @NonNull MappingsRegistry loadFromResourceMappingsDir(String id) throws IOException {
        Path path = PathConstants.MAPPINGS_RESOURCE_PATH.resolve(id + ".json");

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

    private static @NonNull MappingsRegistry loadFromCustomMappingsDir(String id) throws IOException {
        Path path = PathConstants.MAPPINGS_JSONS_PATH.resolve(id + ".json");
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
                LOGGER.error("Failed to resolve custom mappings inheritance for \"{}\"", id, e);
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
            LOGGER.error("Could not access {} mapping file \"{}\" because it does not exist.", custom ? "custom" : "builtin", id);
            return Optional.empty();
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Could not access {} mapping file due to exception: {}", custom ? "custom" : "builtin", id, e);
            return Optional.empty();
        }
    }

    private static @NonNull MappingsRegistry resolveInheritance(MappingsRegistry startRegistry) throws IOException {
        List<MappingsRegistry> registries = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        MappingsRegistry current = startRegistry;
        String namespacePrefix = "custom:";
        while (true) {
            registries.add(current);
            ids.add(namespacePrefix + current.getId());
            if (current.getInherits().isEmpty()) {
                if (registries.size() == 1)
                    LOGGER.info("Resolved no inheritance from mappings: \"{}\"", namespacePrefix + current.getId());
                else
                    LOGGER.info("Resolved inheritance of mappings \"{}\" with a chain of: {}", namespacePrefix + current.getId(), String.join(" -> ", ids));
                break;
            } else {
                boolean inheritsCustom = current.getInherits().startsWith("custom:");
                String idToGet = MappingsIdResolutionUtils.removeNamespaceFromId(current.getInherits());
                Optional<MappingsRegistry> newRegistry = getRegistryFrom(inheritsCustom, idToGet);
                namespacePrefix = inheritsCustom ? "custom:" : "builtin:";
                if (newRegistry.isEmpty())
                    throw new IOException("Failed to resolve inheritance of " + (inheritsCustom ? "custom" : "builtin") + " mappings \"" + idToGet + "\" by mappings \"" + current.getId() + "\"");
                if (ids.contains(namespacePrefix + newRegistry.get().getId()))
                    throw new IOException("Duplicate inheritance of " + (inheritsCustom ? "custom" : "builtin") + " mappings \"" + idToGet + "\" by mappings \"" + current.getId() + "\" in chain of: " + registries);
                current = newRegistry.get();
            }
        }
        LOGGER.debug("Resolved inheritance chain of {} ({}) from registries: {\n{}\n}", ids, registries.stream().map(MappingsRegistry::hashCode).toArray(), String.join("\n--------------------\n", registries.stream().map(MappingsRegistry::toString).toList()));
        return MappingsInheritanceManager.merge(registries.reversed());
    }

    public static void tryMakeConfigFiles() {
        File configDirectory = PathConstants.MAPPINGS_JSONS_PATH.toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            boolean s = configDirectory.mkdirs();
            if (!s)
                LOGGER.error("Could not create mappings config directory at: {}", configDirectory);
            else
                LOGGER.info("Created mappings config directory at: {}", configDirectory.getAbsolutePath());
        }
        File activeMappingsFile = PathConstants.ACTIVE_MAPPINGS_FILE_PATH.toFile();
        if (!activeMappingsFile.exists() || !activeMappingsFile.isFile()) {
            try {
                boolean s = activeMappingsFile.createNewFile();
                if (!s)
                    LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath());
                else
                    LOGGER.info("Created active mappings file at: {}", activeMappingsFile.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath(), e);
            }
        }
    }

    public static @NonNull List<String> getAvailableOptions(boolean namespacedIds) {
        List<String> options = new ArrayList<>();

        File configDirectory = PathConstants.MAPPINGS_JSONS_PATH.toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            tryMakeConfigFiles();
            return options;
        }

        File[] files = configDirectory.listFiles();
        if (files == null)
            return options;

        for (File file : files)
            if (file.getName().endsWith(".json"))
                options.add((namespacedIds ? "custom:" : "") + FilenameUtils.removeExtension(file.getName()));

        return options;
    }
}
