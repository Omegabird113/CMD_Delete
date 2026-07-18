package io.github.omegabird113.cmd_delete.mappings;

public enum MappingsType {
    CUSTOM("custom:"),
    BUILTIN("builtin:"),
    DEFAULT("");

    public final String prefix;

    MappingsType(String name) {
        this.prefix = name;
    }
}
