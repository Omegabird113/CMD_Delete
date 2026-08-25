/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.utils.Os;
import net.minecraft.client.input.KeyEvent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static io.github.omegabird113.cmd_delete.actions.NavAction.NONE;

public record NavMappings(@NonNull MappingsRegistry registry) {
    @Contract(pure = true)
    public @NonNull NavAction getAction(final @NonNull KeyCombo keyCombo) {
        final NavAction action = registry.get(keyCombo);
        if (action == null)
            return NONE;
        if (action.overrideMode() && (Boolean.FALSE.equals(registry.featureFlags().overrideVanillaNavigation()) || CmdDeleteClient.FORCE_PREVENT_OVERRIDE_MODE))
            return NONE;
        return action;
    }

    @Contract(pure = true)
    public @NonNull NavAction getAction(final @NonNull KeyEvent event, final @NonNull Window window) {
        final int key = event.key();
        final boolean shift = event.hasShiftDown();
        final boolean altOption = event.hasAltDown();
        final boolean control = event.hasControlDown();
        final boolean superCommand = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SUPER);

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
    public double getCoverage() {
        final long total = Arrays.stream(NavAction.values())
                .filter(action -> action != NONE)
                .count();
        final int support = getPossibleActions().length;
        return ((double) support) / total;
    }
}
