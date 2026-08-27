package io.github.omegabird113.cmd_delete.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.command.ClientCommandSource;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NeoForgePlatform implements IPlatform {
    private CommandRegistration<?> commandRegistration;

    public NeoForgePlatform() {
        NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, this::registerClientCommand);
    }

    @SuppressWarnings("unchecked")
    private <S extends SharedSuggestionProvider> void registerClientCommand(final RegisterClientCommandsEvent event) {
        if (commandRegistration == null)
            throw new IllegalStateException("Client command registration was requested before the platform was initialized");
        ((CommandRegistration<S>) commandRegistration).register((CommandDispatcher<S>) event.getDispatcher());
    }

    @Override
    public <S extends SharedSuggestionProvider> void registerClientCommand(final @NonNull CommandRegistration<S> registration) {
        commandRegistration = registration;
        ClientCommandSource.setFeedback((source, component) -> ((CommandSourceStack) source).sendSuccess(() -> component, false));
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
        String devMappings = System.getProperty("cmd_delete.dev.mappings");
        if (devMappings != null) {
            Path path = Paths.get(devMappings).resolve("mappings");
            if (Files.isDirectory(path))
                return path;
            throw new IllegalStateException("Configured development mappings directory does not exist: " + path);
        }

        try {
            Path tempDir = Files.createTempDirectory("cmd-delete-mappings");

            for (String mappings : MappingsInfoCollectionUtils.getBuiltinMappingsNamespacedIdsList().stream().map(MappingsIdResolutionUtils::removeNamespaceFromId).toList()) {
                try (InputStream in = NeoForgePlatform.class.getClassLoader()
                        .getResourceAsStream("mappings/" + mappings + ".json")) {
                    if (in == null)
                        throw new IllegalStateException("Missing bundled mapping: mappings/" + mappings + ".json");
                    Files.copy(in, tempDir.resolve(mappings + ".json"));
                }
            }

            return tempDir;
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract CMD + Delete mappings", e);
        }
    }
}
