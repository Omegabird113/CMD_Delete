package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MappingsRegistry {
    private final Map<KeyCombo, NavAction> registry;
    private final Optional<Map<KeyCombo, NavAction>> disabledRegistry;
    private final List<Os> systems;
    private final String inherits;
    private final String name;
    private final String author;
    private final String description;
    private final String version;
    private final String id;

    MappingsRegistry(Map<KeyCombo, NavAction> registry, Collection<Os> systems, String inherits, String name, String author, String description, String version, String id) {
        this.registry = Map.copyOf(registry);
        this.disabledRegistry = Optional.empty();
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
        this.disabledRegistry = Optional.of(Map.copyOf(disabledRegistry));
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
        return disabledRegistry;
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
}
