package io.github.omegabird113.cmd_delete.neoforge;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import net.neoforged.fml.common.Mod;

@Mod(CmdDeleteClient.MODID)
public final class NeoForgeClientEntrypoint {
    public NeoForgeClientEntrypoint() {
        CmdDeleteClient.start(new NeoForgePlatform());
    }
}
