package io.github.omegabird113.cmd_delete.command;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.JsonParsingUtils;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsJSONManager;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeDecoder;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeGenerator;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static io.github.omegabird113.cmd_delete.command.NavMappingsCommand.LOGGER;

class NavMappingsCommandExecutionUtils {
    static void exportShareCode(@NonNull CommandContext<FabricClientCommandSource> context, boolean custom) {
        final String idStr = StringArgumentType.getString(context, "id");

        final String namespacedId = MappingsIdResolutionUtils.resolveNamespacedId(custom ? MappingsState.Type.CUSTOM : MappingsState.Type.BUILTIN, idStr);
        final String shareCode = ShareCodeGenerator.generate(namespacedId);

        Minecraft.getInstance().keyboardHandler.setClipboard(shareCode);
        context.getSource().sendFeedback(Component.literal("Mappings \"" + (custom ? "custom:" : "builtin:") + idStr + "\" can be shared as: " + shareCode));
    }

    static void exportMappings(@NonNull CommandContext<FabricClientCommandSource> context, boolean custom) throws CommandSyntaxException {
        final String idStr = StringArgumentType.getString(context, "id");
        final String locationStr = StringArgumentType.getString(context, "location");

        final Path newPath = Path.of(locationStr);
        if (!newPath.isAbsolute()) {
            LOGGER.error("New path \"{}\" for {} copy is not absolute", locationStr, custom ? "custom" : "builtin");
            if (custom)
                throw CommandCreationUtils.UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
            else
                throw CommandCreationUtils.UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
        }

        try {
            Files.createDirectories(newPath.getParent());
            if (custom) {
                final Path oldPath = PathConstants.getPathOf(MappingsState.Type.CUSTOM, idStr);
                if (!Files.isRegularFile(oldPath)) {
                    LOGGER.error("Error while reading custom mappings. File does not exist: {}", oldPath.toAbsolutePath());
                    throw CommandCreationUtils.UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
                }
                Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                final String resourceSubPathStr = "/mappings/" + idStr + ".json";
                try (InputStream resourceStream = NavMappingsCommand.class.getResourceAsStream(resourceSubPathStr)) {
                    if (resourceStream == null) {
                        LOGGER.error("Builtin mappings do not exist: {}", resourceSubPathStr);
                        throw CommandCreationUtils.UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
                    }
                    Files.copy(resourceStream, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while {} mappings", custom ? "copying custom" : "exporting builtin", e);
            if (custom)
                throw CommandCreationUtils.UNKNOWN_CUSTOM_MAPPINGS.create(idStr);
            else
                throw CommandCreationUtils.UNKNOWN_BUILTIN_MAPPINGS.create(idStr);
        }

        context.getSource().sendFeedback(Component.literal("Mappings \"" + (custom ? "custom:" : "builtin:") + idStr + "\" copied to path: " + newPath.toAbsolutePath()));
    }

    static void importShareCode(@NonNull CommandContext<FabricClientCommandSource> context, @NonNull String shareCode) throws CommandSyntaxException {
        String decoded;
        try {
            decoded = ShareCodeDecoder.decode(shareCode.trim());

            final JsonObject jsonObject = MappingsJSONManager.GSON.fromJson(decoded, JsonObject.class);
            final JsonObject meta = JsonParsingUtils.requireObject(jsonObject, "meta");
            final String idStr = JsonParsingUtils.requireString(meta, "id");

            final Path toCopyTo = PathConstants.getPathOf(MappingsState.Type.CUSTOM, idStr);
            try (FileWriter writer = new FileWriter(toCopyTo.toFile())) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
            } catch (IOException e) {
                LOGGER.error("Error while importing custom mappings", e);
                throw CommandCreationUtils.FAILED_CUSTOM_MAPPINGS_IMPORT.create(idStr);
            }

            context.getSource().sendFeedback(Component.literal("Custom mappings sharecode imported successfully: " + idStr));
        } catch (IllegalArgumentException | JsonParseException e) {
            LOGGER.error("Invalid share code: {}", shareCode, e);
            throw CommandCreationUtils.INVALID_SHARE_CODE.create(shareCode);
        }
    }
}
