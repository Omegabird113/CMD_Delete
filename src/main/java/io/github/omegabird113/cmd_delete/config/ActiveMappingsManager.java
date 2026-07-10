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
import java.util.Optional;

import static io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils.removeNamespaceFromId;
import static io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils.resolveType;

public final class ActiveMappingsManager {
    private static final Logger LOGGER = LoggingManager.getLogger(ActiveMappingsManager.class);

    public @Nullable MappingsState tryResolveCustomMappings(@NonNull String id) {
        final Optional<NavMappings> mappings = MappingsJSONManager.tryLoadCustomMappings(id);
        return mappings.map(navMappings -> new MappingsState(navMappings, MappingsState.Type.CUSTOM, id)).orElse(null);
    }

    public @Nullable MappingsState tryResolveBuiltinMappings(@NonNull String id, MappingsState.@NonNull Type type) {
        final Optional<NavMappings> mappings = MappingsJSONManager.tryLoadBuiltinMappings(id);
        if (mappings.isEmpty())
            return null;
        if (type == MappingsState.Type.DEFAULT)
            id = "";
        return new MappingsState(mappings.get(), type, id);
    }

    @NonNull String resolveDefaultMappingsNonNamespacedId() {
        if (Os.USING == Os.MAC)
            return "mac";
        else
            return "windows_linux";
    }

    String removeNamespaceFromId(String namespacedId) {
        return namespacedId.replaceFirst("custom:|builtin:", "");
    }

    public MappingsState resolveMappings(String namespacedId) {
        String id = removeNamespaceFromId(namespacedId);
        MappingsState.Type type = resolveType(namespacedId);
        MappingsState mappingsState = switch (type) {
            case CUSTOM -> tryResolveCustomMappings(id);
            case BUILTIN -> new MappingsState(resolveOsMappings(id), type, id);
            case DEFAULT -> new MappingsState(resolveDefaultMappings(), type, id);
        };
        if (mappingsState == null)
            return tryResolveBuiltinMappings(resolveDefaultMappingsNonNamespacedId(), MappingsState.Type.DEFAULT);
        return mappingsState;
    }

    void writeActiveMappings(String namespacedId) throws IOException {
        Files.createDirectories(PathConstants.getActiveMappingsFilePath().getParent());
        Files.writeString(PathConstants.getActiveMappingsFilePath(), namespacedId);
    }

    @NonNull String readActiveMappings() throws IOException {
        return Files.readString(PathConstants.getActiveMappingsFilePath());
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
