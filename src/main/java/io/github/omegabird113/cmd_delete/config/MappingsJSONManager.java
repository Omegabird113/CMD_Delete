package io.github.omegabird113.cmd_delete.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;
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
        final Path path = PathConstants.getMappingsResourcePath().resolve(id + ".json");

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(MappingsRegistry.class, new MappingsJSONDeserializer())
                .create();

        try (java.io.BufferedReader reader = Files.newBufferedReader(path)) {
            final MappingsRegistry registry = gson.fromJson(reader, MappingsRegistry.class);
            if (!registry.id().equals(id))
                throw new JsonParseException("Builtin mappings id \"" + registry.id() + "\" does not match filename \"" + id + "\"");
            return registry;
        }
    }

    private static @NonNull MappingsRegistry loadFromCustomMappingsDir(String id) throws IOException {
        final Path path = PathConstants.getMappingsJSONPath().resolve(id + ".json");
        if (!Files.exists(path))
            throw new FileNotFoundException("Custom mapping file not found at: " + path);

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(MappingsRegistry.class, new MappingsJSONDeserializer())
                .create();

        try (java.io.BufferedReader reader = Files.newBufferedReader(path)) {
            final MappingsRegistry registry = gson.fromJson(reader, MappingsRegistry.class);
            if (!registry.id().equals(id))
                throw new JsonParseException("Custom mappings id \"" + registry.id() + "\" does not match filename \"" + id + "\"");
            return registry;
        }
    }

    public static Optional<NavMappings> tryLoadCustomMappings(String id) {
        final Optional<MappingsRegistry> registry = getRegistryFrom(true, id);
        if (registry.isPresent()) {
            try {
                final MappingsRegistry resolved = resolveInheritance(registry.get());
                return Optional.of(new NavMappings(resolved));
            } catch (IOException e) {
                LOGGER.error("Failed to resolve custom mappings inheritance for \"{}\"", id, e);
                return Optional.empty();
            }
        } else
            return Optional.empty();
    }

    public static Optional<NavMappings> tryLoadBuiltinMappings(String id) {
        final Optional<MappingsRegistry> registry = getRegistryFrom(false, id);
        return registry.map(NavMappings::new);
    }

    public static Optional<MappingsRegistry> getRegistryFrom(boolean custom, String id) {
        try {
            final MappingsRegistry registry = custom ? loadFromCustomMappingsDir(id) : loadFromResourceMappingsDir(id);
            return Optional.of(registry);
        } catch (FileNotFoundException ignored) {
            LOGGER.error("Could not access {} mapping file \"{}\" because it does not exist.", custom ? "custom" : "builtin", id);
            return Optional.empty();
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Could not access {} mapping file due to exception: {}", custom ? "custom" : "builtin", id, e);
            return Optional.empty();
        }
    }

    private static @NonNull MappingsRegistry resolveInheritance(MappingsRegistry startRegistry) throws IOException {
        final List<MappingsRegistry> registries = new ArrayList<>();
        final List<String> ids = new ArrayList<>();
        MappingsRegistry current = startRegistry;
        String namespacePrefix = "custom:";
        while (true) {
            registries.add(current);
            ids.add(namespacePrefix + current.id());
            if (current.inherits().isEmpty()) {
                if (registries.size() == 1)
                    LOGGER.info("Resolved no inheritance from mappings: \"{}\"", namespacePrefix + current.id());
                else
                    LOGGER.info("Resolved inheritance of mappings \"{}\" with a chain of: {}", namespacePrefix + current.id(), String.join(" -> ", ids));
                break;
            } else {
                boolean inheritsCustom = current.inherits().startsWith("custom:");
                final String idToGet = MappingsIdResolutionUtils.removeNamespaceFromId(current.inherits());
                final Optional<MappingsRegistry> newRegistry = getRegistryFrom(inheritsCustom, idToGet);
                namespacePrefix = inheritsCustom ? "custom:" : "builtin:";
                if (newRegistry.isEmpty())
                    throw new IOException("Failed to resolve inheritance of " + (inheritsCustom ? "custom" : "builtin") + " mappings \"" + idToGet + "\" by mappings \"" + current.id() + "\" because the inherited registry couldn't load.");
                if (ids.contains(namespacePrefix + newRegistry.get().id()))
                    throw new IOException("Duplicate inheritance of " + (inheritsCustom ? "custom" : "builtin") + " mappings \"" + idToGet + "\" by mappings \"" + current.id() + "\" in chain of: " + String.join(" -> ", ids));
                current = newRegistry.get();
            }
        }
        LOGGER.debug("Resolved inheritance chain of {} ({}) from registries: {\n{}\n}", ids, registries.stream().map(MappingsRegistry::hashCode).toArray(), String.join("\n--------------------\n", registries.stream().map(MappingsRegistry::toString).toList()));
        return MappingsInheritanceManager.merge(reverseList(registries));
    }

    public static <T> List<T> reverseList(List<T> list) {
        if (list == null || list.size() <= 1)
            return List.of();

        ArrayList<T> internal = new ArrayList<>(list);
        int left = 0;
        int right = list.size() - 1;
        while (left < right) {
            T item = internal.get(left);
            internal.set(left, list.get(right));
            internal.set(right, item);

            left++;
            right--;
        }
        return List.copyOf(internal);
    }

    public static void tryMakeConfigFiles() {
        final File configDirectory = PathConstants.getMappingsJSONPath().toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            boolean s = configDirectory.mkdirs();
            if (!s)
                LOGGER.error("Could not create mappings config directory at: {}", configDirectory);
            else
                LOGGER.info("Created mappings config directory at: {}", configDirectory.getAbsolutePath());
        }
        final File activeMappingsFile = PathConstants.getActiveMappingsFilePath().toFile();
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

    @Contract(pure = true)
    public static @NonNull List<String> getAvailableOptions(boolean namespacedIds) {
        final List<String> options = new ArrayList<>();

        final File configDirectory = PathConstants.getMappingsJSONPath().toFile();
        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
            tryMakeConfigFiles();
            return options;
        }

        final File[] files = configDirectory.listFiles();
        if (files == null)
            return options;

        for (File file : files)
            if (file.getName().endsWith(".json"))
                options.add((namespacedIds ? "custom:" : "") + FilenameUtils.removeExtension(file.getName()));

        return options;
    }
}
