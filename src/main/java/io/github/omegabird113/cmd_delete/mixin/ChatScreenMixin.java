package io.github.omegabird113.cmd_delete.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.command.LocalCommandManager;
import io.github.omegabird113.cmd_delete.command.LocalCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Shadow
    protected EditBox input;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void cmd_delete$handleLocalCommand(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if (i == GLFW.GLFW_KEY_ENTER || i == GLFW.GLFW_KEY_KP_ENTER) {
            String text = this.input.getValue();
            
            if (text != null && text.startsWith("/")) {
                String command = text.substring(1);
                
                try {
                    LocalCommandSource source = message -> Minecraft.getInstance().gui.getChat().addMessage(message);
                    int result = LocalCommandManager.DISPATCHER.execute(command, source);
                    if (result > 0) {
                        this.input.setValue("");
                        Minecraft.getInstance().setScreen(null);
                        cir.setReturnValue(true);
                    }
                } catch (CommandSyntaxException e) {
                    CmdDeleteClient.LOGGER.debug("Command failed: {}", e.getMessage());
                }
            }
        }
    }
}
