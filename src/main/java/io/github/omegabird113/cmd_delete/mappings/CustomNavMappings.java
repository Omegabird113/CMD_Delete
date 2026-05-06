package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistryKey;

import java.util.ArrayList;

import static io.github.omegabird113.cmd_delete.actions.NavAction.*;

public class CustomNavMappings implements INavMappings {
    private static CustomMappingsRegistry registry = new CustomMappingsRegistry();

    public static void setRegistry(CustomMappingsRegistry registry) {
        CustomNavMappings.registry = registry;
    }

    @Override
    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        CustomMappingsRegistryKey registryKey = new CustomMappingsRegistryKey(key, shift, altOption, control, superCommand);

        NavAction action = registry.get(registryKey);
        if (action != null) {
            return action;
        }

        return NONE;
    }

    @Override
    public NavAction[] getPossibleActions() {
        ArrayList<NavAction> supported = new ArrayList<>();
        for (NavAction action : registry.getValues()) {
            if (!supported.contains(action) && action != NONE) {
                supported.add(action);
            }
        }
        return supported.toArray(new NavAction[0]);
    }

    @Override
    public Os[] getMappingsSupportedSystems() {
        return registry.getSystems().toArray(new Os[0]);
    }
}
