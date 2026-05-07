package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistry;
import io.github.omegabird113.cmd_delete.config.registry.CustomMappingsRegistryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static io.github.omegabird113.cmd_delete.actions.NavAction.*;

public class CustomNavMappings implements INavMappings {
    private CustomMappingsRegistry registry = new CustomMappingsRegistry();

    public void setRegistry(CustomMappingsRegistry registry) {
        this.registry = registry;
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
        return Arrays.stream(registry.getValues())
                .filter(action -> action != NONE)
                .distinct().toArray(NavAction[]::new);
    }

    @Override
    public Os[] getMappingsSupportedSystems() {
        return registry.getSystems().stream()
                .distinct()
                .toArray(Os[]::new);
    }
}
