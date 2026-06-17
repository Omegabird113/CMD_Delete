package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomMappingsRegistry {
    private final Map<KeyCombo, NavAction> registry;
    private final List<Os> systems;
    private final String name;
    private final String author;
    private final String description;
    private final String version;
    private final String id;

    CustomMappingsRegistry(Map<KeyCombo, NavAction> registry, Collection<Os> systems, String name, String author, String description, String version, String id) {
        this.registry = Map.copyOf(registry);
        this.systems = List.copyOf(systems);
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.id = id;
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
