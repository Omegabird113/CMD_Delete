package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.io.IOException;
import java.nio.file.Files;

public class ActiveMappingsManager {
    private final NavMappings navMappings;

    private final Os system;

    public ActiveMappingsManager(NavMappings navMappings, Os system) {
        this.navMappings = navMappings;
        this.system = system;
    }

    public MappingsState tryResolveCustomMappings(String id) {
        if (!CustomMappingsJSONManager.tryLoadCustomMappings(id, navMappings)) {
            return null;
        }
        return new MappingsState(navMappings, MappingsState.Type.CUSTOM, id);
    }

    public MappingsState tryResolveBuiltinMappings(String id, MappingsState.Type type) {
        if (!CustomMappingsJSONManager.tryLoadBuiltinMappings(id, navMappings)) {
            return null;
        }
        return new MappingsState(navMappings, type, id);
    }

    public String resolveNamespacedId(MappingsState.Type type, String id) {
        String prefixText = switch (type) {
            case CUSTOM -> "custom:";
            case BUILTIN -> "builtin:";
            case DEFAULT -> "";
        };
        return prefixText + id;
    }

    public String resolveNamespacedId(MappingsState.Type type, Os os) {
        String prefixText = switch (type) {
            case CUSTOM -> "custom:";
            case BUILTIN -> "builtin:";
            case DEFAULT -> "";
        };
        String osText = switch (os) {
            case WINDOWS, LINUX -> "windows_linux";
            case MAC -> "mac";
        };
        return prefixText + osText;
    }

    public String resolveNamespacedId(MappingsState mappingState) {
        MappingsState.Type type = mappingState.type();
        String id = mappingState.id();
        return resolveNamespacedId(type, id);
    }

    public MappingsState.Type resolveType(String namespacedId) {
        if (namespacedId.startsWith("custom:")) {
            return MappingsState.Type.CUSTOM;
        } else if (namespacedId.startsWith("builtin:")) {
            return MappingsState.Type.BUILTIN;
        } else {
            return MappingsState.Type.DEFAULT;
        }
    }

    String removeNamespaceFromId(String namespacedId) {
        return namespacedId.replaceFirst("custom:|builtin:", "");
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
            case MappingsState.Type.BUILTIN -> tryResolveBuiltinMappings(removeNamespaceFromId(id), MappingsState.Type.BUILTIN);
            case MappingsState.Type.DEFAULT -> tryResolveBuiltinMappings(resolveDefaultMappingsNonNamespacedId(), MappingsState.Type.DEFAULT);
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
            CmdDeleteClient.LOGGER.error("Error while loading active mappings from file: {}", e.getMessage());
        }
        return resolveMappings(namespacedId);
    }

    public void trySaveMappings(String namespacedId) {
        try {
            writeActiveMappings(namespacedId);
        } catch (IOException e) {
            CmdDeleteClient.LOGGER.error("Error while saving active mappings to file: {}", e.getMessage());
        }
    }
}
