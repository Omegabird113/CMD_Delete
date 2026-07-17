package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public final class MappingsIdResolutionUtils {
    private MappingsIdResolutionUtils() {
    }

    @Contract(pure = true)
    public static @NonNull String resolveNamespacedId(MappingsState.@NonNull Type type, String id) {
        return switch (type) {
            case CUSTOM -> "custom:";
            case BUILTIN -> "builtin:";
            case DEFAULT -> "";
        } + id;
    }

    public static @NonNull String resolveNamespacedId(@NonNull MappingsState mappingState) {
        return resolveNamespacedId(mappingState.type(), mappingState.id());
    }

    @Contract(pure = true)
    public static MappingsState.Type resolveType(@NonNull String namespacedId) {
        if (namespacedId.startsWith("custom:"))
            return MappingsState.Type.CUSTOM;
        if (namespacedId.startsWith("builtin:"))
            return MappingsState.Type.BUILTIN;
        return MappingsState.Type.DEFAULT;
    }

    @Contract(pure = true)
    public static @NonNull String removeNamespaceFromId(@NonNull String namespacedId) {
        if (namespacedId.startsWith("custom:"))
            return namespacedId.substring("custom:".length());
        if (namespacedId.startsWith("builtin:"))
            return namespacedId.substring("builtin:".length());
        return namespacedId;
    }
}
