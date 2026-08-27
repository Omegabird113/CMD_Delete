package io.github.omegabird113.cmd_delete.fabric;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jspecify.annotations.NonNull;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FabricPlatform implements IPlatform {
    @Override
    @SuppressWarnings("unchecked")
    public <S extends SharedSuggestionProvider> void registerClientCommand(final @NonNull CommandRegistration<S> registration) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> registration.register((CommandDispatcher<S>) dispatcher));
    }

    @Override
    public @NonNull String getModVersion() {
        return FabricLoader.getInstance().getModContainer(CmdDeleteClient.MODID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("<unknown>");
    }

    @Override
    public @NonNull Path getGamePath() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public @NonNull Path getResourcePath() {
        final Path modResourcePath = FabricLoader.getInstance().getModContainer(CmdDeleteClient.MODID)
                .orElseThrow().findPath("mappings/").orElseThrow();
        if (Files.isDirectory(modResourcePath.resolve("mappings")))
            return modResourcePath;
        try {
            //noinspection DataFlowIssue
            return Path.of(CmdDeleteClient.class.getResource("/mappings").toURI()).getParent();
        } catch (NullPointerException | URISyntaxException e) {
            throw new IllegalStateException("Could not locate CMD + Delete's bundled mappings", e);
        }
    }
}
