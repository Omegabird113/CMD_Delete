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
        if (action != null)
            if (ActionOffsetUtils.isOverrideAction(action))
                if (registry.featureFlags().overrideVanillaNavigation())
                    return action;
                else
                    return NONE;
            else
                return action;
        return NONE;
    }

    @Contract(pure = true)
    public NavAction getAction(@NonNull KeyEvent event) {
        final int key = event.key();
        final boolean shift = event.hasShiftDown();
        final boolean alt = event.hasAltDown();
        final boolean control = event.hasControlDown();
        final boolean windows = InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_LGUI) || InputConstants.isKeyDown(SDLScancode.SDL_SCANCODE_RGUI);

        final KeyCombo keyCombo = new KeyCombo(key, shift, alt, control, windows);
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
        final int total = Arrays.stream(NavAction.values())
                .filter(action -> action != NONE)
                .toArray(NavAction[]::new)
                .length;
        final int support = this.getPossibleActions().length;
        return ((float) support) / total;
    }
}
