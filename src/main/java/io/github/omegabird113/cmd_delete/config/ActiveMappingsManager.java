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
    private static final Path GAME_PATH = FabricLoader.getInstance().getGameDir();
    private static final Path ACTIVE_FILE_PATH = GAME_PATH.resolve("config/cmd_delete/.active_mappings");

    private final INavMappings windowsLinux;
    private final INavMappings mac;
    private final CustomNavMappings custom;

    private final Os system;

    public ActiveMappingsManager(INavMappings windowsLinux, INavMappings mac, CustomNavMappings custom, Os system) {
        this.windowsLinux = windowsLinux;
        this.mac = mac;
        this.custom = custom;
        this.system = system;
    }

    INavMappings resolveDefaultMappings() {
        if (system == Os.MAC)
            return mac;
        return windowsLinux;
    }

    public MappingsState tryResolveCustomMappings(String id) {
        if (!CustomMappingsJSONManager.tryLoadCustomMappings(id, custom)) {
            return null;
        }
        return new MappingsState(custom, MappingsState.Type.CUSTOM, id);
    }

    INavMappings resolveOsMappings(String os) {
        os = os.toLowerCase(Locale.ROOT);
        if (os.equals("mac"))
            return mac;
        return windowsLinux;
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
        MappingsState mappingsState = switch (type) {
            case MappingsState.Type.CUSTOM -> tryResolveCustomMappings(id);
            case MappingsState.Type.BUILTIN -> new MappingsState(resolveOsMappings(id), type, id);
            case MappingsState.Type.DEFAULT -> new MappingsState(resolveDefaultMappings(), type, id);
        };
        if (mappingsState == null)
            return new MappingsState(resolveDefaultMappings(), MappingsState.Type.DEFAULT, "");
        return mappingsState;
    }

    void writeActiveMappings(String namespacedId) throws IOException {
        Files.createDirectories(ACTIVE_FILE_PATH.getParent());
        Files.writeString(ACTIVE_FILE_PATH, namespacedId);
    }

    String readActiveMappings() throws IOException {
        return Files.readString(ACTIVE_FILE_PATH);
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
