package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public record MappingsState(@NonNull NavMappings mappings, @NonNull Type type, @NonNull String id) {
    @Override
    public @NonNull String toString() {
        return switch (type) {
            case CUSTOM -> "Custom";
            case BUILTIN -> "Builtin";
            case DEFAULT -> "Default";
        } + " mappings id \""
                + id
                + " with registry:\n\""
                + mappings.registry()
                + "\"";
    }

    public enum Type {
        CUSTOM("custom:"),
        BUILTIN("builtin:"),
        DEFAULT("");

        public final String prefix;

        Type(String name) {
            this.prefix = name;
        }
    }
}
