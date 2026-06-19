package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MappingsRegistry {
    private final Map<KeyCombo, NavAction> registry;
    private final Map<KeyCombo, NavAction> disabledRegistry;
    private final List<Os> systems;
    private final String inherits;
    private final String name;
    private final String author;
    private final String description;
    private final String version;
    private final String id;

    MappingsRegistry(Map<KeyCombo, NavAction> registry, Collection<Os> systems, String inherits, String name, String author, String description, String version, String id) {
        this.registry = Map.copyOf(registry);
        this.disabledRegistry = null;
        this.systems = List.copyOf(systems);
        this.inherits = inherits;
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.id = id;
    }

    MappingsRegistry(Map<KeyCombo, NavAction> registry, Map<KeyCombo, NavAction> disabledRegistry, Collection<Os> systems, String inherits, String name, String author, String description, String version, String id) {
        this.registry = Map.copyOf(registry);
        this.disabledRegistry = Map.copyOf(disabledRegistry);
        this.systems = List.copyOf(systems);
        this.inherits = inherits;
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.id = id;
    }

    public Map<KeyCombo, NavAction> getInternalRegistry() {
        return registry;
    }

    public Optional<Map<KeyCombo, NavAction>> getInternalDisabledRegistry() {
        return Optional.ofNullable(disabledRegistry);
    }

    public NavAction get(KeyCombo key) {
        return registry.get(key);
    }

    public NavAction[] getValues() {
        return registry.values().toArray(NavAction[]::new);
    }

    public List<Os> getSystems() {
        return systems;
    }

    public String getInherits() {
        return inherits;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return registry.size();
    }

    @Override
    public String toString() {
        return String.format("""
                        MappingsRegistry(
                        name="%s",
                        author="%s",
                        description="%s",
                        version="%s",
                        id="%s",
                        inherits="%s",
                        hashcode=%d
                        registry="%s",
                        disabledRegistry="%s")""",
                name, author, description, version, id, inherits,
                hashCode(), registry.toString(),
                (disabledRegistry == null ? "null" : disabledRegistry.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MappingsRegistry mr))
            return false;
        else {
            return mr.id.equals(this.id)
                    && mr.version.equals(this.version)
                    && mr.author.equals(this.author)
                    && mr.description.equals(this.description)
                    && mr.registry.equals(this.registry)
                    && mr.name.equals(this.name)
                    && ((mr.disabledRegistry == null || this.disabledRegistry == null) ? disabledRegistry == mr.disabledRegistry : disabledRegistry.equals(mr.disabledRegistry))
                    && mr.systems.equals(this.systems)
                    && mr.inherits.equals(this.inherits);
        }
    }

    @Override
    public int hashCode() {
        return registry.hashCode()
                + (disabledRegistry == null ? 0 : disabledRegistry.hashCode())
                + systems.hashCode()
                + inherits.hashCode()
                + name.hashCode()
                + author.hashCode()
                + description.hashCode()
                + version.hashCode()
                + id.hashCode();
    }
}
