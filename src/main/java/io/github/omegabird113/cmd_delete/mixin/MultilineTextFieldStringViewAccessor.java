package io.github.omegabird113.cmd_delete.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.client.gui.components.MultilineTextField$StringView")
public interface MultilineTextFieldStringViewAccessor {
    @Accessor("beginIndex")
    int cmd_delete$getBeginIndex();

    @Accessor("endIndex")
    int cmd_delete$getEndIndex();
}
