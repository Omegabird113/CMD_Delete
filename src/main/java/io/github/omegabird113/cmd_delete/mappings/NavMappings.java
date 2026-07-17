package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import net.minecraft.client.input.KeyEvent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.lwjgl.sdl.SDLScancode;

import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.actions.NavAction.NONE;

public record NavMappings(@NonNull MappingsRegistry registry) {
    @Contract(pure = true)
    public NavAction getAction(@NonNull KeyCombo keyCombo) {
        final NavAction action = registry.get(keyCombo);
        if (action == null)
            return NONE;

        if (ActionOffsetUtils.isOverrideAction(action)
                && !registry.featureFlags().overrideVanillaNavigation())
            return NONE;

        return action;
    }

    @Contract(pure = true)
    public NavAction getAction(@NonNull KeyEvent event) {
        final int key = event.key();
        final boolean shift = event.hasShiftDown();
        final boolean altOption = event.hasAltDown();
        final boolean control = event.hasControlDown();
        final boolean superCommand = InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_LGUI) || InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_RGUI);

        final KeyCombo keyCombo = new KeyCombo(key, shift, altOption, control, superCommand);
        return getAction(keyCombo);
    }

    @Contract(pure = true)
    public NavAction @NonNull [] getPossibleActions() {
        return Arrays.stream(registry.getValues())
                .filter(action -> action != NONE)
                .distinct()
                .toArray(NavAction[]::new);
    }

    @Contract(pure = true)
    public Os @NonNull [] getMappingsSupportedSystems() {
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
