package io.github.omegabird113.cmddelete.actions.mappings;

import io.github.omegabird113.cmddelete.actions.ActionConstant;
import io.github.omegabird113.cmddelete.actions.OsConstant;
import net.minecraft.client.input.KeyEvent;

public interface INavMapping {
    ActionConstant getAction(int key, boolean shift, boolean altOption, boolean control, boolean superCommand);
    ActionConstant getAction(KeyEvent event);
    ActionConstant[] getPossibleActions();
    OsConstant getMappingOs();
}
