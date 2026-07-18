package io.github.omegabird113.cmd_delete.config.data;

import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public final class MappingsIdResolutionUtils {
    private MappingsIdResolutionUtils() {
    }

    @Contract(pure = true)
    public static @NonNull String resolveNamespacedId(@NonNull MappingsType mappingsType, String id) {
        return switch (mappingsType) {
            case CUSTOM -> MappingsType.CUSTOM.prefix;
            case BUILTIN -> MappingsType.BUILTIN.prefix;
            case DEFAULT -> "";
        } + id;
    }

    public static @NonNull String resolveNamespacedId(@NonNull MappingsState mappingState) {
        return resolveNamespacedId(mappingState.mappingsType(), mappingState.id());
    }

    @Contract(pure = true)
    public static MappingsType resolveType(@NonNull String namespacedId) {
        if (namespacedId.startsWith(MappingsType.CUSTOM.prefix))
            return MappingsType.CUSTOM;
        if (namespacedId.startsWith(MappingsType.BUILTIN.prefix))
            return MappingsType.BUILTIN;
        return MappingsType.DEFAULT;
    }

    @Contract(pure = true)
    public static @NonNull String removeNamespaceFromId(@NonNull String namespacedId) {
        if (namespacedId.startsWith(MappingsType.CUSTOM.prefix))
            return namespacedId.substring(MappingsType.CUSTOM.prefix.length());
        if (namespacedId.startsWith(MappingsType.BUILTIN.prefix))
            return namespacedId.substring(MappingsType.BUILTIN.prefix.length());
        return namespacedId;
    }
}
