package io.github.omegabird113.cmd_delete.forge;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public final class ForgePlatform implements IPlatform {
    public static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(ForgePlatform.class);
    private ICommandRegistration<?> commandRegistration;
    private final @NonNull Path resourcePath = setupResourcePath();
    public ForgePlatform() { MinecraftForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> registerClientCommand(event)); }
    @SuppressWarnings("unchecked")
    private void registerClientCommand(RegisterClientCommandsEvent event) {
        if (commandRegistration == null) throw new IllegalStateException("Client command registration was requested before the platform was initialized");
        ((ICommandRegistration) commandRegistration).register((CommandDispatcher) event.getDispatcher());
    }
    @Override public <S extends SharedSuggestionProvider> void registerClientCommand(@NonNull ICommandRegistration<S> registration) { commandRegistration = registration; }
    @Override public @NonNull BiConsumer<SharedSuggestionProvider, Component> getFeedbackMethod() { return (source, component) -> ((CommandSourceStack) source).sendSuccess(() -> component, false); }
    @Override public @NotNull String getPlatformName() { return "Forge Platform"; }
    @Override public @NonNull String getModVersion() { return ModList.get().getModContainerById(CmdDeleteClient.MODID).orElseThrow().getModInfo().getVersion().toString(); }
    @Override public @NonNull Path getGamePath() { return FMLPaths.GAMEDIR.get(); }
    private @NotNull Path setupResourcePath() {
        String devMappings = System.getProperty("cmd_delete.dev.mappings");
        if (devMappings != null) { Path path = Paths.get(devMappings).resolve("mappings"); if (Files.isDirectory(path)) return path; throw new IllegalStateException("Configured development mappings directory does not exist: " + path); }
        try {
            Path tempDir = Files.createTempDirectory("cmd_delete-builtin-mappings");
            for (String mappings : MappingsInfoCollectionUtils.getBuiltinMappingsNamespacedIdsList().stream().map(MappingsIdResolutionUtils::removeNamespaceFromId).toList())
                try (InputStream in = ForgePlatform.class.getClassLoader().getResourceAsStream("mappings/" + mappings + ".json")) {
                    if (in == null) throw new IllegalStateException("Missing bundled mapping: mappings/" + mappings + ".json");
                    Files.copy(in, tempDir.resolve(mappings + ".json"));
                }
            return tempDir;
        } catch (IOException e) { throw new RuntimeException("Failed to extract CMD + Delete mappings", e); }
    }
    @Override public @NonNull Path getResourcePath() { return resourcePath; }
    @Override public @NotNull Logger getPlatformLogger() { return LOGGER; }
}
