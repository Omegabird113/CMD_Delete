package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.CustomMappingsRegistryKey;

import static io.github.omegabird113.cmd_delete.actions.NavAction.*;

public class CustomNavMappings implements INavMappings {
    @Override
    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        CustomMappingsRegistryKey registryKey = new CustomMappingsRegistryKey(key, shift, altOption, control, superCommand);
        CustomMappingsRegistry registry = CustomMappingsRegistry.getCurrent();

        NavAction action = registry.get(registryKey);
        if (action != null) {
            return action;
        }

        return NONE;
    }

    @Override
    public NavAction[] getPossibleActions() {
        CustomMappingsRegistry registry = CustomMappingsRegistry.getCurrent();
        return registry.getValues();
    }

    @Override
    public Os[] getMappingsSupportedSystems() {
        CustomMappingsRegistry registry = CustomMappingsRegistry.getCurrent();
        return registry.getSystems().toArray(new Os[3]);
    }
}
