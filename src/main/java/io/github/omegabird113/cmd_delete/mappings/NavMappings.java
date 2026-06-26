package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.KeyCombo;
import io.github.omegabird113.cmd_delete.config.MappingsRegistry;
import net.minecraft.client.input.KeyEvent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.actions.NavAction.NONE;

public final class NavMappings {
    private MappingsRegistry registry = null;

    public MappingsRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(MappingsRegistry registry) {
        this.registry = registry;
    }

    @Contract(pure = true)
    public NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand) {
        if (registry == null)
            return NONE;
        KeyCombo registryKey = new KeyCombo(key, shift, altOption, control, superCommand);
        NavAction action = registry.get(registryKey);
        if (action != null)
            return action;
        return NONE;
    }

    @Contract(pure = true)
    public NavAction getAction(@NonNull KeyEvent event, Window window) {
        int key = event.key();
        boolean shift = event.hasShiftDown();
        boolean alt = event.hasAltDown();
        boolean control = event.hasControlDown();
        boolean windows = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SUPER);

        return getAction(key, shift, alt, control, windows);
    }

    @Contract(pure = true)
    public NavAction @NonNull [] getPossibleActions() {
        if (registry == null)
            return new NavAction[0];
        return Arrays.stream(registry.getValues())
                .filter(action -> action != NONE)
                .distinct()
                .toArray(NavAction[]::new);
    }

    @Contract(pure = true)
    public Os @NonNull [] getMappingsSupportedSystems() {
        if (registry == null)
            return new Os[0];
        return registry.getSystems().stream()
                .distinct()
                .toArray(Os[]::new);
    }

    @Contract(pure = true)
    public float getCoverage() {
        int total = Arrays.stream(NavAction.values())
                .filter(action -> action != NavAction.NONE)
                .toArray(NavAction[]::new)
                .length;
        int support = this.getPossibleActions().length;
        return ((float) support) / total;
    }
}
