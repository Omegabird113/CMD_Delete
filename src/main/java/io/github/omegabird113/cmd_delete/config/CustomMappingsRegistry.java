package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.mappings.Os;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomMappingsRegistry {
    private static CustomMappingsRegistry current;

    private final Map<CustomMappingsRegistryKey, NavAction> registry = new HashMap<>();
    private final ArrayList<Os> systems = new ArrayList<>(3);

    public static CustomMappingsRegistry getCurrent() {
        return current;
    }

    public static void setCurrent(CustomMappingsRegistry current) {
        CustomMappingsRegistry.current = current;
    }

    public void register(CustomMappingsRegistryKey key, NavAction action) {
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

    public void AddSystem(Os system) {
        systems.add(system);
    }

    public void RemoveSystem(Os system) {
        systems.remove(system);
    }
}
