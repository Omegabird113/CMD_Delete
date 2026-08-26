package io.github.omegabird113.cmd_delete.fabric;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CmdDeleteClient.start(new FabricPlatform());
    }
}
