package io.github.omegabird113.cmd_delete.mixin;

import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextFieldHelper.class)
public interface TextFieldHelperAccessor {
    @Accessor("cursorPos")
    int cmd_delete$getCursorPosRaw();

    @Accessor("cursorPos")
    void cmd_delete$setCursorPosRaw(int pos);

    @Accessor("selectionPos")
    void cmd_delete$setSelectionPosRaw(int pos);
}

