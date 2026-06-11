package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.mappings.INavMappings;

public record MappingsState(INavMappings mappings, Type type, String id) {

    public enum Type {
        CUSTOM, BUILTIN, DEFAULT
    }
}
