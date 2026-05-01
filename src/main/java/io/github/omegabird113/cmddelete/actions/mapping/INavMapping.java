package io.github.omegabird113.cmddelete.actions.mapping;

import com.mojang.blaze3d.platform.Window;
import io.github.omegabird113.cmddelete.actions.ActionConstant;
import io.github.omegabird113.cmddelete.actions.OsConstant;
import net.minecraft.client.input.KeyEvent;

public interface INavMapping {
    ActionConstant getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand);
    ActionConstant getAction(KeyEvent event, Window window);
    ActionConstant[] getPossibleActions();
    OsConstant getMappingOs();
}
