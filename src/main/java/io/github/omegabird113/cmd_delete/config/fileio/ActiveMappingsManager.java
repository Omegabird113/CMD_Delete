package io.github.omegabird113.cmd_delete.config.fileio;

import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
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
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(ActiveMappingsManager.class);

    private ActiveMappingsManager() {
    }

    public static @Nullable MappingsState tryResolveCustomMappings(@NonNull String id) {
        final Optional<NavMappings> mappings = MappingsJSONManager.tryLoadCustomMappings(id);
        return mappings.map(navMappings -> new MappingsState(navMappings, MappingsType.CUSTOM, id)).orElse(null);
    }

    public static @Nullable MappingsState tryResolveBuiltinMappings(@NonNull String id, @NonNull MappingsType mappingsType) {
        final Optional<NavMappings> mappings = MappingsJSONManager.tryLoadBuiltinMappings(id);
        if (mappings.isEmpty())
            return null;
        final String idToGet = mappingsType == MappingsType.DEFAULT ? "" : id;
        return new MappingsState(mappings.get(), mappingsType, idToGet);
    }

    static @NonNull String resolveDefaultMappingsNonNamespacedId() {
        return (Os.USING == Os.MAC)
                ? "mac"
                : "windows_linux";
    }

    public static @Nullable MappingsState resolveMappings(@NonNull String namespacedId) {
        final String id = removeNamespaceFromId(namespacedId);
        final MappingsType mappingsType = resolveType(namespacedId);
        final String defaultMappingsId = resolveDefaultMappingsNonNamespacedId();
        final MappingsState mappingsState = switch (mappingsType) {
            case CUSTOM -> tryResolveCustomMappings(id);
            case BUILTIN -> tryResolveBuiltinMappings(id, MappingsType.BUILTIN);
            case DEFAULT -> tryResolveBuiltinMappings(defaultMappingsId, MappingsType.DEFAULT);
        };
        if (mappingsState == null)
            return tryResolveBuiltinMappings(defaultMappingsId, MappingsType.DEFAULT);
        return mappingsState;
    }

    static void writeActiveMappings(@NonNull String namespacedId) throws IOException {
        Files.createDirectories(PathConstants.getActiveMappingsFilePath().getParent());
        Files.writeString(PathConstants.getActiveMappingsFilePath(), namespacedId);
    }

    static @NonNull String readActiveMappings() throws IOException {
        return Files.readString(PathConstants.getActiveMappingsFilePath());
    }

    @Nullable
    public static MappingsState tryGetMappings() {
        String namespacedId = "";
        try {
            namespacedId = readActiveMappings();
        } catch (IOException e) {
            LOGGER.error("Error while loading active mappings from file: ", e);
        }
        return resolveMappings(namespacedId);
    }

    public static void trySaveMappings(@NonNull String namespacedId) {
        try {
            writeActiveMappings(namespacedId);
        } catch (IOException e) {
            LOGGER.error("Error while saving active mappings to file: {}", e.getMessage());
        }
    }
}
