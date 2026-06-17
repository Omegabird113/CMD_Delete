package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.KeyCombo;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.actions.NavAction.NONE;

public final class NavMappings {
    private MappingsRegistry registry;

    public MappingsRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(MappingsRegistry registry) {
        this.registry = registry;
    }

    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        if (registry == null)
            return NONE;
        KeyCombo registryKey = new KeyCombo(key, shift, altOption, control, superCommand);
        NavAction action = registry.get(registryKey);
        if (action != null)
            return action;
        return NONE;
    }

    public NavAction getAction(KeyEvent event, Window window) {
        int key = event.key();
        boolean shift = event.hasShiftDown();
        boolean alt = event.hasAltDown();
        boolean control = event.hasControlDown();
        boolean windows = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SUPER);

        return getAction(key, shift, alt, control, windows);
    }

    public NavAction[] getPossibleActions() {
        return Arrays.stream(registry.getValues())
                .filter(action -> action != NONE)
                .distinct()
                .toArray(NavAction[]::new);
    }

    public Os[] getMappingsSupportedSystems() {
        return registry.getSystems().stream()
                .distinct()
                .toArray(Os[]::new);
    }
}
