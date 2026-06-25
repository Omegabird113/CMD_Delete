package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.Os;

public final class MappingsIdResolutionUtils {
    private MappingsIdResolutionUtils() {
    }

    public static String resolveNamespacedId(MappingsState.Type type, String id) {
        String prefixText = switch (type) {
            case CUSTOM -> "custom:";
            case BUILTIN -> "builtin:";
            case DEFAULT -> "";
        };
        return prefixText + id;
    }

    public static String resolveNamespacedId(MappingsState.Type type, Os os) {
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

    public static String resolveNamespacedId(MappingsState mappingState) {
        MappingsState.Type type = mappingState.type();
        String id = mappingState.id();
        return resolveNamespacedId(type, id);
    }

    public static MappingsState.Type resolveType(String namespacedId) {
        if (namespacedId.startsWith("custom:"))
            return MappingsState.Type.CUSTOM;
        else if (namespacedId.startsWith("builtin:"))
            return MappingsState.Type.BUILTIN;
        else
            return MappingsState.Type.DEFAULT;
    }

    public static String removeNamespaceFromId(String namespacedId) {
        return namespacedId.replaceFirst("custom:|builtin:", "");
    }
}
