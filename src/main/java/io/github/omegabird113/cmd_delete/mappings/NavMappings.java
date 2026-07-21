package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.actions.NavAction.NONE;

public record NavMappings(@NonNull MappingsRegistry registry) {
    @Contract(pure = true)
    public @NonNull NavAction getAction(@NonNull KeyCombo keyCombo) {
        final NavAction action = registry.get(keyCombo);
        if (action == null)
            return NONE;

        if (ActionOffsetUtils.isOverrideAction(action)
                && Boolean.FALSE.equals(registry.featureFlags().overrideVanillaNavigation()))
            return NONE;

        return action;
    }

    @Contract(pure = true)
    public @NonNull NavAction getAction(int key, Window window) {
        final boolean shift = InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);
        final boolean altOption = InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_RIGHT_ALT);
        final boolean control = InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_RIGHT_CONTROL);
        final boolean superCommand = InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_RIGHT_SUPER);

        final KeyCombo keyCombo = new KeyCombo(key, shift, altOption, control, superCommand);
        return getAction(keyCombo);
    }

    @Contract(pure = true)
    public @NonNull NavAction @NonNull [] getPossibleActions() {
        return Arrays.stream(registry.getValues())
                .filter(action -> action != NONE)
                .distinct()
                .toArray(NavAction[]::new);
    }

    @Contract(pure = true)
    public @NonNull Os @NonNull [] getMappingsSupportedSystems() {
        return registry.systems().stream()
                .distinct()
                .toArray(Os[]::new);
    }

    @Contract(pure = true)
    public float getCoverage() {
        final long total = Arrays.stream(NavAction.values())
                .filter(action -> action != NONE)
                .count();
        final int support = getPossibleActions().length;
        return ((float) support) / total;
    }
}
