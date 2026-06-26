package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public final class MappingsIdResolutionUtils {
    private MappingsIdResolutionUtils() {
    }

    @Contract(pure = true)
    public static @NonNull String resolveNamespacedId(MappingsState.@NonNull Type type, String id) {
        String prefixText = switch (type) {
            case CUSTOM -> "custom:";
            case BUILTIN -> "builtin:";
            case DEFAULT -> "";
        };
        return prefixText + id;
    }

    @Contract(pure = true)
    public static @NonNull String resolveNamespacedId(MappingsState.@NonNull Type type, Os os) {
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

    public static @NonNull String resolveNamespacedId(@NonNull MappingsState mappingState) {
        MappingsState.Type type = mappingState.type();
        String id = mappingState.id();
        return resolveNamespacedId(type, id);
    }

    @Contract(pure = true)
    public static MappingsState.Type resolveType(@NonNull String namespacedId) {
        if (namespacedId.startsWith("custom:"))
            return MappingsState.Type.CUSTOM;
        else if (namespacedId.startsWith("builtin:"))
            return MappingsState.Type.BUILTIN;
        else
            return MappingsState.Type.DEFAULT;
    }

    @Contract(pure = true)
    public static @NonNull String removeNamespaceFromId(@NonNull String namespacedId) {
        return namespacedId.replaceFirst("custom:|builtin:", "");
    }
}
