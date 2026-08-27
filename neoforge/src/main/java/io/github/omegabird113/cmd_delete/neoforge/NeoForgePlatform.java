package io.github.omegabird113.cmd_delete.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.IPlatform;
import io.github.omegabird113.cmd_delete.command.ClientCommandSource;
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
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        final Path modFile = ModList.get().getModFileById(CmdDeleteClient.MODID)
                .getFile().getFilePath();
        try (JarFile jarFile = new JarFile(modFile.toFile())) {
            final Path temp = Files.createTempDirectory("cmd-delete-mappings");

            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (!entry.getName().startsWith("mappings/"))
                    continue;

                final Path destination = temp.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(destination);
                    continue;
                }

                Files.createDirectories(destination.getParent());
                try (InputStream input = jarFile.getInputStream(entry)) {
                    Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return temp;
        } catch (IOException e) {
            throw new IllegalStateException("Could not locate CMD + Delete's bundled mappings", e);
        }
    }
}
