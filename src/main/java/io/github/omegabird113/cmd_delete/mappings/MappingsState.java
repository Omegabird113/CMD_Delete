package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public record MappingsState(@NonNull NavMappings mappings, @NonNull Type type, @NonNull String id) {
    @Override
    public @NonNull String toString() {
        return switch (type) {
            case CUSTOM -> "Custom";
            case BUILTIN -> "Builtin";
            case DEFAULT -> "Default";
        }
                + " mappings id \""
                + id
                + "\" (class: "
                + mappings
                + ") with registry:\n"
                + mappings.getRegistry().toString();
    }

    public enum Type {
        CUSTOM, BUILTIN, DEFAULT
    }
}
