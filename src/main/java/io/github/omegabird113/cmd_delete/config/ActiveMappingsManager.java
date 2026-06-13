package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.CustomNavMappings;
import io.github.omegabird113.cmd_delete.mappings.INavMappings;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class ActiveMappingsManager {
    private static final Path gamePath = FabricLoader.getInstance().getGameDir();
    private static final Path activeFilePath = gamePath.resolve("config/cmd_delete/.active_mappings");

    private final INavMappings WINDOWS_LINUX;
    private final INavMappings MAC;
    private final CustomNavMappings CUSTOM;

    private final Os system;

    public ActiveMappingsManager(INavMappings windowsLinux, INavMappings mac, CustomNavMappings custom, Os system) {
        WINDOWS_LINUX = windowsLinux;
        MAC = mac;
        CUSTOM = custom;
        this.system = system;
    }

    INavMappings resolveDefaultMappings() {
        if (system == Os.MAC)
            return MAC;
        return WINDOWS_LINUX;
    }

    INavMappings tryResolveCustomMappingsElseDefault(String id) {
        return CustomMappingsJSONManager.tryLoadCustomMappingsElse(id, CUSTOM, resolveDefaultMappings());
    }

    public MappingsState tryResolveCustomMappings(String id) {
        if (!CustomMappingsJSONManager.tryLoadCustomMappings(id, CUSTOM)) {
            return null;
        }
        return new MappingsState(CUSTOM, MappingsState.Type.CUSTOM, id);
    }

    INavMappings resolveOsMappings(String os) {
        os = os.toLowerCase(Locale.ROOT);
        if (os.equals("mac"))
            return MAC;
        return WINDOWS_LINUX;
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

    public MappingsState resolveMappings(String namespacedId) {
        String id = removeNamespaceFromId(namespacedId);
        MappingsState.Type type = resolveType(namespacedId);
        INavMappings mappings = switch (type) {
            case MappingsState.Type.CUSTOM -> tryResolveCustomMappingsElseDefault(id);
            case MappingsState.Type.BUILTIN -> resolveOsMappings(id);
            case MappingsState.Type.DEFAULT -> resolveDefaultMappings();
        };
        return new MappingsState(mappings, type, id);
    }

    void writeActiveMappings(String namespacedId) throws IOException {
        Files.createDirectories(activeFilePath.getParent());
        Files.writeString(activeFilePath, namespacedId);
    }

    String readActiveMappings() throws IOException {
        return Files.readString(activeFilePath);
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
