package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.INavMappings;
import io.github.omegabird113.cmd_delete.mappings.Os;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ActiveMappingsManager {
    private static final Path gamePath = FabricLoader.getInstance().getGameDir();
    private static final Path activeFilePath = gamePath.resolve("config/cmd_delete/.active_mappings");

    private final INavMappings WINDOWS;
    private final INavMappings MAC;
    private final INavMappings LINUX;
    private final INavMappings CUSTOM;

    private final Os system;

    public enum Type {
        custom, builtin, defaultMappings
    }

    public ActiveMappingsManager(INavMappings windows, INavMappings mac, INavMappings linux, INavMappings custom, Os system) {
        WINDOWS = windows;
        MAC = mac;
        LINUX = linux;
        CUSTOM = custom;
        this.system = system;
    }

    INavMappings resolveDefaultMappings() {
        if (system == Os.MAC)
            return MAC;
        else if (system == Os.WINDOWS)
            return WINDOWS;
        return LINUX;
    }

    INavMappings tryResolveCustomMappingsElseDefault(String id) {
        return CustomMappingsJSONManager.tryLoadCustomMappingsElse(id, CUSTOM, resolveDefaultMappings());
    }

    INavMappings resolveOsMappings(Os os) {
        if (os == Os.MAC)
            return MAC;
        else if (os == Os.WINDOWS)
            return WINDOWS;
        return LINUX;
    }

    INavMappings resolveOsMappings(String os) {
        if (os.equals("mac"))
            return resolveOsMappings(Os.MAC);
        else if (os.equals("windows"))
            return resolveOsMappings(Os.WINDOWS);
        return resolveOsMappings(Os.LINUX);
    }

    public String resolveNamespacedId(Type type, String id) {
        String prefixText = switch(type) {
            case custom -> "custom:";
            case builtin -> "builtin:";
            case defaultMappings -> "";
        };
        return prefixText + id;
    }

    public String resolveNamespacedId(Type type, Os os) {
        String prefixText = switch(type) {
            case custom -> "custom:";
            case builtin -> "builtin:";
            case defaultMappings -> "";
        };
        return prefixText + os.toString();
    }

    public String resolveNamespacedId(MappingState mappingState) {
        Type type = mappingState.type();
        String id = mappingState.id();
        return resolveNamespacedId(type, id);
    }

    public Type resolveType(String namespacedId) {
        if (namespacedId.startsWith("custom:")) {
            return Type.custom;
        } else if (namespacedId.startsWith("builtin:")) {
            return Type.builtin;
        } else {
            return Type.defaultMappings;
        }
    }

    String removeNamespaceFromId(String namespacedId) {
        return namespacedId.replaceFirst("custom:|builtin:", "");
    }

    public MappingState resolveMappings(String namespacedId) {
        String id = removeNamespaceFromId(namespacedId);
        Type type = resolveType(namespacedId);
        INavMappings mappings = switch (type) {
            case Type.custom -> tryResolveCustomMappingsElseDefault(id);
            case Type.builtin -> resolveOsMappings(id);
            default -> resolveDefaultMappings();
        };
        return new MappingState(mappings, type, id);
    }

    void writeActiveMappings(String namespacedId) throws IOException {
        Files.writeString(activeFilePath, namespacedId);
    }

    String readActiveMappings() throws IOException {
        return Files.readString(activeFilePath);
    }

    public MappingState tryGetMappings() {
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
