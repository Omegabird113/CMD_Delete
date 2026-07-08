package io.github.omegabird113.cmd_delete.mappings;

public class MappingsState {
    private final INavMappings mappings;
    private final Type type;
    private final String id;

    public MappingsState(INavMappings mappings, Type type, String id) {
        this.mappings = mappings;
        this.type = type;
        this.id = id;
    }

    public INavMappings mappings() {
        return mappings;
    }

    public Type type() {
        return type;
    }

    public String id() {
        return id;
    }

    public enum Type {
        CUSTOM, BUILTIN, DEFAULT
    }
}
