package io.github.omegabird113.cmd_delete.mappings;

import org.jspecify.annotations.NonNull;

public enum MappingsType {
    CUSTOM("custom:", "Custom"),
    BUILTIN("builtin:", "Builtin"),
    DEFAULT("", "Default");

    public final @NonNull String prefix;
    public final @NonNull String commonName;

    MappingsType(@NonNull String prefix, @NonNull String commonName) {
        this.prefix = prefix;
        this.commonName = commonName;
    }
}
