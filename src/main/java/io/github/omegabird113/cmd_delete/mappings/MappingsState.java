package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public record MappingsState(@NonNull NavMappings mappings, @NonNull MappingsType mappingsType, @NonNull String id) {
    @Override
    public @NonNull String toString() {
        return switch (mappingsType) {
            case CUSTOM -> "Custom";
            case BUILTIN -> "Builtin";
            case DEFAULT -> "Default";
        } + " mappings id \""
                + id
                + " with registry:\n\""
                + mappings.registry()
                + "\"";
    }

}
