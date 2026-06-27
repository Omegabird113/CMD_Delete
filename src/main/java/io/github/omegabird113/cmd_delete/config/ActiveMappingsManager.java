package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;

import static io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils.removeNamespaceFromId;
import static io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils.resolveType;

public class ActiveMappingsManager {
    private static final Logger LOGGER = LoggingManager.getLogger(ActiveMappingsManager.class);
    private final @NonNull NavMappings navMappings;
    private final @NonNull Os system;

    public ActiveMappingsManager(@NonNull NavMappings navMappings, @NonNull Os system) {
        this.navMappings = navMappings;
        this.system = system;
    }

    public @Nullable MappingsState tryResolveCustomMappings(@NonNull String id) {
        if (!MappingsJSONManager.tryLoadCustomMappings(id, navMappings))
            return null;
        return new MappingsState(navMappings, MappingsState.Type.CUSTOM, id);
    }

    public @Nullable MappingsState tryResolveBuiltinMappings(@NonNull String id, MappingsState.@NonNull Type type) {
        if (!MappingsJSONManager.tryLoadBuiltinMappings(id, navMappings))
            return null;
        if (type == MappingsState.Type.DEFAULT)
            id = "";
        return new MappingsState(navMappings, type, id);
    }

    @NonNull String resolveDefaultMappingsNonNamespacedId() {
        if (system == Os.MAC)
            return "mac";
        else
            return "windows_linux";
    }

    public @Nullable MappingsState resolveMappings(@NonNull String namespacedId) {
        String id = removeNamespaceFromId(namespacedId);
        MappingsState.Type type = resolveType(namespacedId);
        MappingsState mappingsState = switch (type) {
            case CUSTOM -> tryResolveCustomMappings(id);
            case BUILTIN -> tryResolveBuiltinMappings(removeNamespaceFromId(id), MappingsState.Type.BUILTIN);
            case DEFAULT ->
                    tryResolveBuiltinMappings(resolveDefaultMappingsNonNamespacedId(), MappingsState.Type.DEFAULT);
        };
        if (mappingsState == null)
            return tryResolveBuiltinMappings(resolveDefaultMappingsNonNamespacedId(), MappingsState.Type.DEFAULT);
        return mappingsState;
    }

    void writeActiveMappings(String namespacedId) throws IOException {
        Files.createDirectories(PathConstants.ACTIVE_MAPPINGS_FILE_PATH.getParent());
        Files.writeString(PathConstants.ACTIVE_MAPPINGS_FILE_PATH, namespacedId);
    }

    @NonNull String readActiveMappings() throws IOException {
        return Files.readString(PathConstants.ACTIVE_MAPPINGS_FILE_PATH);
    }

    @Nullable
    public MappingsState tryGetMappings() {
        String namespacedId = "";
        try {
            namespacedId = readActiveMappings();
        } catch (IOException e) {
            LOGGER.error("Error while loading active mappings from file: {}", e.getMessage());
        }
        return resolveMappings(namespacedId);
    }

    public void trySaveMappings(String namespacedId) {
        try {
            writeActiveMappings(namespacedId);
        } catch (IOException e) {
            LOGGER.error("Error while saving active mappings to file: {}", e.getMessage());
        }
    }
}
