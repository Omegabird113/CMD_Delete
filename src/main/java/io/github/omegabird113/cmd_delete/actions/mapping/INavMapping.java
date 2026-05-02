package io.github.omegabird113.cmd_delete.actions.mapping;

import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.Os;
import net.minecraft.client.input.KeyEvent;

public interface INavMapping {
    NavAction getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand);
    NavAction getAction(KeyEvent event, Window window);
    NavAction[] getPossibleActions();
    Os getMappingOs();
}
