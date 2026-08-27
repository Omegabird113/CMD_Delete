package io.github.omegabird113.cmd_delete.neoforge;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CmdDeleteClient.MODID, dist = Dist.CLIENT)
public final class NeoForgeCmdDelete {
    private static boolean started;

    public NeoForgeCmdDelete() {
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, _ -> {
            //noinspection ConstantValue
            if (!started && Minecraft.getInstance() != null) {
                started = true;
                CmdDeleteClient.start(new NeoForgePlatform());
            }
        });
    }
}
