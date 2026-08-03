package io.github.omegabird113.cmd_delete.config.fileio;

import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import io.github.omegabird113.cmd_delete.utils.Os;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils.removeNamespaceFromId;
import static io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils.resolveType;

public final class ActiveMappingsManager {
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(ActiveMappingsManager.class);

    private ActiveMappingsManager() {
    }

    public static @Nullable MappingsState tryResolveCustomMappings(final @NonNull String id) {
        final Optional<NavMappings> mappings = MappingsJSONManager.tryLoadMappings(id, true);
        return mappings.map(navMappings -> new MappingsState(navMappings, MappingsType.CUSTOM, id)).orElse(null);
    }

    public static @Nullable MappingsState tryResolveBuiltinMappings(final @NonNull String id, final @NonNull MappingsType mappingsType) {
        final Optional<NavMappings> mappings = MappingsJSONManager.tryLoadMappings(id, false);
        if (mappings.isEmpty())
            return null;
        final String idToGet = mappingsType == MappingsType.DEFAULT ? "" : id;
        if (idToGet.equals("emacs_windows_linux") || idToGet.equals("emacs_mac") || idToGet.equals("readline"))
            LOGGER.warn("These mappings are not completely accurate to the conventions of the software they emulate. They do their best to provide similar behaviour to cause less issues with muscle memory, but they do not fully re-work Minecraft to provide the full experience of the control scheme.");
        return new MappingsState(mappings.get(), mappingsType, idToGet);
    }

    public static @NonNull String resolveDefaultMappingsNonNamespacedId() {
        return (Os.USING == Os.MAC)
                ? "mac"
                : "windows_linux";
    }

    public static @Nullable MappingsState resolveMappings(final @NonNull String namespacedId) {
        final String id = removeNamespaceFromId(namespacedId);
        final MappingsType mappingsType = resolveType(namespacedId);
        final String defaultMappingsId = resolveDefaultMappingsNonNamespacedId();
        return switch (mappingsType) {
            case CUSTOM -> tryResolveCustomMappings(id);
            case BUILTIN -> tryResolveBuiltinMappings(id, MappingsType.BUILTIN);
            case DEFAULT -> tryResolveBuiltinMappings(defaultMappingsId, MappingsType.DEFAULT);
        };
    }

    public static @NonNull MappingsState resolveMappingsWithDefaultFallback(final @NonNull String namespacedId) {
        final String id = removeNamespaceFromId(namespacedId);
        final MappingsType mappingsType = resolveType(namespacedId);
        final String defaultMappingsId = resolveDefaultMappingsNonNamespacedId();
        MappingsState newState = switch (mappingsType) {
            case CUSTOM -> tryResolveCustomMappings(id);
            case BUILTIN -> tryResolveBuiltinMappings(id, MappingsType.BUILTIN);
            case DEFAULT -> tryResolveBuiltinMappings(defaultMappingsId, MappingsType.DEFAULT);
        };
        if (newState == null) {
            if (mappingsType == MappingsType.DEFAULT)
                throw new IllegalStateException("Failed to resolve default mappings.");
            newState = tryResolveBuiltinMappings(defaultMappingsId, MappingsType.DEFAULT);
            if (newState == null)
                throw new IllegalStateException("Failed to resolve default mappings.");
        }
        return newState;
    }

    public static void writeActiveMappings(final @NonNull String namespacedId) throws IOException {
        Files.createDirectories(PathConstants.getActiveMappingsFilePath().getParent());
        Files.writeString(PathConstants.getActiveMappingsFilePath(), namespacedId);
    }

    public static @NonNull String readActiveMappings() throws IOException {
        return Files.readString(PathConstants.getActiveMappingsFilePath());
    }

    public static @Nullable MappingsState tryGetMappings() {
        String namespacedId = "";
        try {
            namespacedId = readActiveMappings();
        } catch (IOException e) {
            LOGGER.error("Error while loading active mappings from file: ", e);
        }
        return resolveMappings(namespacedId);
    }

    public static void trySaveMappings(final @NonNull String namespacedId) {
        try {
            writeActiveMappings(namespacedId);
        } catch (IOException e) {
            LOGGER.error("Error while saving active mappings to file: {}", e.getMessage());
        }
    }
}
