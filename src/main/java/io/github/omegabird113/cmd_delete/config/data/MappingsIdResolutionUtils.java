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
            case CUSTOM -> MappingsState.Type.CUSTOM.prefix;
            case BUILTIN -> MappingsState.Type.BUILTIN.prefix;
            case DEFAULT -> "";
        } + id;
    }

    public static @NonNull String resolveNamespacedId(@NonNull MappingsState mappingState) {
        return resolveNamespacedId(mappingState.type(), mappingState.id());
    }

    @Contract(pure = true)
    public static MappingsState.Type resolveType(@NonNull String namespacedId) {
        if (namespacedId.startsWith(MappingsState.Type.CUSTOM.prefix))
            return MappingsState.Type.CUSTOM;
        if (namespacedId.startsWith(MappingsState.Type.BUILTIN.prefix))
            return MappingsState.Type.BUILTIN;
        return MappingsState.Type.DEFAULT;
    }

    @Contract(pure = true)
    public static @NonNull String removeNamespaceFromId(@NonNull String namespacedId) {
        if (namespacedId.startsWith(MappingsState.Type.CUSTOM.prefix))
            return namespacedId.substring(MappingsState.Type.CUSTOM.prefix.length());
        if (namespacedId.startsWith(MappingsState.Type.BUILTIN.prefix))
            return namespacedId.substring(MappingsState.Type.BUILTIN.prefix.length());
        return namespacedId;
    }
}
