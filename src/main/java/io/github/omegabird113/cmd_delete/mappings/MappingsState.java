package io.github.omegabird113.cmd_delete.mappings;

public record MappingsState(INavMappings mappings, Type type, String id) {
    public enum Type {
        CUSTOM, BUILTIN, DEFAULT
    }
}
