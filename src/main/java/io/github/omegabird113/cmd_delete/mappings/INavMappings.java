package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.gui.screens.Screen;

public interface INavMappings {
    NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand);

    default NavAction getAction(int key, Window window) {
        boolean shift = Screen.hasShiftDown();
        boolean alt = Screen.hasAltDown();
        boolean control = Screen.hasControlDown();

        boolean windows = InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(window.getWindow(), GLFW.GLFW_KEY_RIGHT_SUPER);

        return getAction(key, shift, alt, control, windows);
    }

    NavAction[] getPossibleActions();

    Os[] getMappingsSupportedSystems();
}
