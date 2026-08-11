/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.config.fileio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
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
    public static final @NonNull Gson GSON = new GsonBuilder()
            .registerTypeAdapter(MappingsRegistry.class, new MappingsJSONDeserializer())
            .create();
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(MappingsJSONManager.class);

    private MappingsJSONManager() {
    }

    public static @NonNull MappingsRegistry loadFromDir(final @NonNull String id, final boolean custom) throws IOException {
        final Path path = PathConstants.getPathOf(MappingsType.fromIfCustom(custom), id);

        if (!Files.isRegularFile(path))
            throw new FileNotFoundException(MappingsType.fromIfCustom(custom).commonName() + " mapping file not found at: " + path);

        try (java.io.BufferedReader reader = Files.newBufferedReader(path)) {
            final MappingsRegistry registry = GSON.fromJson(reader, MappingsRegistry.class);
            if (!registry.id().equals(id))
                throw new JsonParseException(MappingsType.fromIfCustom(custom).commonName() + " mappings id \"" + registry.id() + "\" does not match filename \"" + id + "\"");
            return registry;
        }
    }

    public static @NonNull Optional<NavMappings> tryLoadMappings(final @NonNull String id, final boolean custom) {
        final Optional<MappingsRegistry> registry = getRegistryFrom(id, custom);
        if (registry.isPresent())
            try {
                final MappingsRegistry resolved = resolveInheritance(registry.get());
                return Optional.of(new NavMappings(resolved));
            } catch (IOException e) {
                LOGGER.error("Failed to resolve {} mappings inheritance for \"{}\"", MappingsType.fromIfCustom(custom).commonName(), id, e);
                return Optional.empty();
            }
        else
            return Optional.empty();
    }

    public static @NonNull Optional<MappingsRegistry> getRegistryFrom(final @NonNull String id, final boolean custom) {
        final String typeCName = MappingsType.fromIfCustom(custom).commonName();
        try {
            final MappingsRegistry registry = loadFromDir(id, custom);
            return Optional.of(registry);
        } catch (FileNotFoundException ignored) {
            LOGGER.error("Could not access {} mapping file \"{}\" (at \"{}\") because it does not exist.", typeCName, id, PathConstants.getPathOf(MappingsType.fromIfCustom(custom), id));
            return Optional.empty();
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Could not access {} mapping file due to exception: {}", typeCName, id, e);
            return Optional.empty();
        }
    }

    public static @NonNull MappingsRegistry resolveInheritance(final @NonNull MappingsRegistry startRegistry) throws IOException {
        final List<MappingsRegistry> registries = new ArrayList<>();
        final List<String> ids = new ArrayList<>();
        MappingsRegistry current = startRegistry;
        String namespacePrefix = MappingsType.CUSTOM.prefix();
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
                final boolean inheritsCustom = current.inherits().startsWith(MappingsType.CUSTOM.prefix());
                final String idToGet = MappingsIdResolutionUtils.removeNamespaceFromId(current.inherits());
                final Optional<MappingsRegistry> newRegistry = getRegistryFrom(idToGet, inheritsCustom);
                namespacePrefix = MappingsType.fromIfCustom(inheritsCustom).prefix();
                if (newRegistry.isEmpty())
                    throw new IOException("Failed to resolve inheritance of " + MappingsType.fromIfCustom(inheritsCustom).commonName() + " mappings \"" + idToGet + "\" by mappings \"" + current.id() + "\" because the inherited registry couldn't load.");
                if (ids.contains(namespacePrefix + newRegistry.get().id()))
                    throw new IOException("Duplicate inheritance of " + MappingsType.fromIfCustom(inheritsCustom).commonName() + " mappings \"" + idToGet + "\" by mappings \"" + current.id() + "\" in chain of: " + String.join(" -> ", ids));
                current = newRegistry.get();
            }
        }
        LoggingManager.traceLog(LOGGER, "Resolved inheritance chain of {} ({}) from registries: {\n{}\n}", ids, registries.stream().map(MappingsRegistry::hashCode).toArray(), String.join("\n--------------------\n", registries.stream().map(MappingsRegistry::toString).toList()));
        return MappingsInheritanceManager.merge(reverseList(registries));
    }

    public static <T> List<T> reverseList(List<T> list) {
        if (list == null || list.isEmpty())
            return List.of();
        if (list.size() == 1)
            return List.copyOf(list);

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
            final boolean s = configDirectory.mkdirs();
            if (!s)
                LOGGER.error("Could not create mappings config directory at: {}", configDirectory);
            else
                LOGGER.info("Created mappings config directory at: {}", configDirectory.getAbsolutePath());
        }
        final File activeMappingsFile = PathConstants.getActiveMappingsFilePath().toFile();
        if (!activeMappingsFile.exists() || !activeMappingsFile.isFile())
            try {
                final boolean s = activeMappingsFile.createNewFile();
                if (!s)
                    LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath());
                else
                    LOGGER.info("Created active mappings file at: {}", activeMappingsFile.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("Could not create active mappings file at: {}", activeMappingsFile.getAbsolutePath(), e);
            }
    }

    @Contract(pure = true)
    public static @NonNull List<String> getAvailableOptions(final boolean namespacedIds) {
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
                options.add((namespacedIds ? MappingsType.CUSTOM.prefix() : "") + FilenameUtils.removeExtension(file.getName()));

        return options;
    }
}
