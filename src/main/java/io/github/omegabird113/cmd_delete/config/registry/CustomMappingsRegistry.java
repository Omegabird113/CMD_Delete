package io.github.omegabird113.cmd_delete.config.registry;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomMappingsRegistry {
    private final Map<CustomMappingsRegistryKey, NavAction> registry = new HashMap<>();
    private final ArrayList<Os> systems = new ArrayList<>();
    private String name;
    private String author;
    private String description;
    private String version;
    private String filename;

    public void put(CustomMappingsRegistryKey key, NavAction action) {
        registry.put(key, action);
    }

    public NavAction get(CustomMappingsRegistryKey key) {
        return registry.get(key);
    }

    public NavAction[] getValues() {
        return registry.values().toArray(new NavAction[0]);
    }

    public ArrayList<Os> getSystems() {
        return systems;
    }

    public void setSystems(ArrayList<Os> systems) {
        this.systems.clear();
        this.systems.addAll(systems);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return registry.size();
    }
}
