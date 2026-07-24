package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public enum MappingsType {
    CUSTOM("custom:", "Custom"),
    BUILTIN("builtin:", "Builtin"),
    DEFAULT("", "Default");

    private final @NonNull String prefix;
    private final @NonNull String commonName;

    MappingsType(@NonNull String prefix, @NonNull String commonName) {
        this.prefix = prefix;
        this.commonName = commonName;
    }

    public @NonNull String prefix() {
        return prefix;
    }
    public @NonNull String commonName() {
        return commonName;
    }
}
