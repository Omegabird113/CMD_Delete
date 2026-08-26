package io.github.omegabird113.cmd_delete.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.command.ClientCommandSource;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.NonNull;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NeoForgePlatform implements IPlatform {
    private CommandRegistration<?> commandRegistration;

    public NeoForgePlatform() {
        NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, this::registerClientCommand);
    }

    @SuppressWarnings("unchecked")
    private <S extends net.minecraft.commands.SharedSuggestionProvider> void registerClientCommand(
            final RegisterClientCommandsEvent event
    ) {
        if (commandRegistration == null)
            throw new IllegalStateException("Client command registration was requested before the platform was initialized");
        ((CommandRegistration<S>) commandRegistration)
                .register((CommandDispatcher<S>) event.getDispatcher());
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
    public @NonNull Path getGamePath() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public @NonNull Path getResourcePath() {
        final Path modPath = ModList.get().getModContainerById(CmdDeleteClient.MODID)
                .orElseThrow(() -> new IllegalStateException("CMD + Delete is not present in NeoForge's mod list"))
                .getModInfo().getOwningFile().getFile().getFilePath();
        if (Files.isDirectory(modPath.resolve("mappings")))
            return modPath;

        Path buildResources = modPath;
        for (int i = 0; i < 3 && buildResources.getParent() != null; i++)
            buildResources = buildResources.getParent();
        buildResources = buildResources.resolve("resources/main");
        if (Files.isDirectory(buildResources.resolve("mappings")))
            return buildResources;

        try {
            URL mappingsResource = CmdDeleteClient.class.getResource("/mappings");
            if (mappingsResource == null)
                throw new IllegalStateException("The bundled mappings resource is missing");
            return Path.of(mappingsResource.toURI()).getParent();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not locate CMD + Delete's bundled mappings", e);
        }
    }
}
