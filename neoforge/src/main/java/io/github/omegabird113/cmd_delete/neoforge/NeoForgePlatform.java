package io.github.omegabird113.cmd_delete.neoforge;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.command.ClientCommandSource;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModList;
import com.mojang.brigadier.CommandDispatcher;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

public final class NeoForgePlatform implements IPlatform {
    private CommandRegistration<?> commandRegistration;

    public NeoForgePlatform() {
        NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, event ->
                registerClientCommand(event));
    }

    @SuppressWarnings("unchecked")
    private <S extends net.minecraft.commands.SharedSuggestionProvider> void registerClientCommand(
            final RegisterClientCommandsEvent event
    ) {
        if (commandRegistration == null)
            throw new IllegalStateException("Client command registration was requested before the platform was initialized");
        ((CommandRegistration<S>) (CommandRegistration<?>) commandRegistration)
                .register((CommandDispatcher<S>) (CommandDispatcher<?>) event.getDispatcher());
    }

    @Override
    public <S extends net.minecraft.commands.SharedSuggestionProvider> void registerClientCommand(
            final @NonNull CommandRegistration<S> registration
    ) {
        commandRegistration = registration;
        ClientCommandSource.setFeedback((source, component) ->
                ((net.minecraft.commands.CommandSourceStack) source).sendSuccess(() -> component, false));
    }

    @Override
    public @NonNull String getModVersion() {
        return ModList.get().getModContainerById(CmdDeleteClient.MODID)
                .orElseThrow(() -> new IllegalStateException("CMD + Delete is not present in NeoForge's mod list"))
                .getModInfo().getVersion().toString();
    }

    @Override
    public @NonNull Path getResourcePath() {
        return ModList.get().getModContainerById(CmdDeleteClient.MODID)
                .orElseThrow(() -> new IllegalStateException("CMD + Delete is not present in NeoForge's mod list"))
                .getModInfo().getOwningFile().getFile().getFilePath();
    }
}
