package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;

import static io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils.removeNamespaceFromId;
import static io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils.resolveType;

public class ActiveMappingsManager {
    private static final Logger LOGGER = CmdDeleteClient.getLogger(ActiveMappingsManager.class);
    private final NavMappings navMappings;
    private final Os system;

    public ActiveMappingsManager(NavMappings navMappings, Os system) {
        this.navMappings = navMappings;
        this.system = system;
    }

    public MappingsState tryResolveCustomMappings(String id) {
        if (!MappingsJSONManager.tryLoadCustomMappings(id, navMappings))
            return null;
        return new MappingsState(navMappings, MappingsState.Type.CUSTOM, id);
    }

    public MappingsState tryResolveBuiltinMappings(String id, MappingsState.Type type) {
        if (!MappingsJSONManager.tryLoadBuiltinMappings(id, navMappings))
            return null;
        if (type == MappingsState.Type.DEFAULT)
            id = "";
        return new MappingsState(navMappings, type, id);
    }

    String resolveDefaultMappingsNonNamespacedId() {
        if (system == Os.MAC)
            return "mac";
        else
            return "windows_linux";
    }

    public MappingsState resolveMappings(String namespacedId) {
        String id = removeNamespaceFromId(namespacedId);
        MappingsState.Type type = resolveType(namespacedId);
        MappingsState mappingsState = switch (type) {
            case MappingsState.Type.CUSTOM -> tryResolveCustomMappings(id);
            case MappingsState.Type.BUILTIN ->
                    tryResolveBuiltinMappings(removeNamespaceFromId(id), MappingsState.Type.BUILTIN);
            case MappingsState.Type.DEFAULT ->
                    tryResolveBuiltinMappings(resolveDefaultMappingsNonNamespacedId(), MappingsState.Type.DEFAULT);
        };
        if (mappingsState == null)
            return tryResolveBuiltinMappings(resolveDefaultMappingsNonNamespacedId(), MappingsState.Type.DEFAULT);
        return mappingsState;
    }

    void writeActiveMappings(String namespacedId) throws IOException {
        Files.createDirectories(CmdDeleteClient.ACTIVE_MAPPINGS_FILE_PATH.getParent());
        Files.writeString(CmdDeleteClient.ACTIVE_MAPPINGS_FILE_PATH, namespacedId);
    }

    String readActiveMappings() throws IOException {
        return Files.readString(CmdDeleteClient.ACTIVE_MAPPINGS_FILE_PATH);
    }

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
