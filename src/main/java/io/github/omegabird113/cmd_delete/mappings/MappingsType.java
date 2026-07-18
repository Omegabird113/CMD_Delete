package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public enum MappingsType {
    CUSTOM("custom:"),
    BUILTIN("builtin:"),
    DEFAULT("");

    public final @NonNull String prefix;

    MappingsType(@NonNull String name) {
        this.prefix = name;
    }
}
