package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistryKey;

import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.actions.NavAction.NONE;

public final class CustomNavMappings implements INavMappings {
    private CustomMappingsRegistry registry;

    public CustomMappingsRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(CustomMappingsRegistry registry) {
        this.registry = registry;
    }

    @Override
    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        if (registry == null)
            return NONE;
        CustomMappingsRegistryKey registryKey = new CustomMappingsRegistryKey(key, shift, altOption, control, superCommand);
        NavAction action = registry.get(registryKey);
        if (action != null)
            return action;
        return NONE;
    }

    @Override
    public NavAction[] getPossibleActions() {
        return Arrays.stream(registry.getValues())
                .filter(action -> action != NONE)
                .distinct()
                .toArray(NavAction[]::new);
    }

    @Override
    public Os[] getMappingsSupportedSystems() {
        return registry.getSystems().stream()
                .distinct()
                .toArray(Os[]::new);
    }
}
