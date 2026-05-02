package io.github.omegabird113.cmd_delete.mappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

public interface INavMappings {
    NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand);
    default NavAction getAction(KeyEvent event, Window window) {
        int key = event.key();
        boolean shift = event.hasShiftDown();
        boolean alt = event.hasAltDown();
        boolean control = event.hasControlDown();
        boolean windows = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SUPER) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SUPER);

        return getAction(key, shift, alt, control, windows);
    }
    NavAction[] getPossibleActions();
    Os[] getMappingsSupportedSystems();
}
