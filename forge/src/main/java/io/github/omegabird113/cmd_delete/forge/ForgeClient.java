package io.github.omegabird113.cmd_delete.forge;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(CmdDeleteClient.MODID)
public final class ForgeClient {
    private boolean started;
    public ForgeClient() { MinecraftForge.EVENT_BUS.addListener(this::onClientTick); }
    private void onClientTick(TickEvent.ClientTickEvent event) {
        if (!started && event.phase == TickEvent.Phase.END && Minecraft.getInstance() != null) {
            started = true;
            CmdDeleteClient.start(new ForgePlatform());
        }
    }
}
