package io.github.omegabird113.cmd_delete.config.sharecode;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import org.apache.commons.codec.binary.Base58;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.GZIPOutputStream;

public final class ShareCodeGenerator {
    static final Base58 BASE_58 = new Base58();
    private static final Logger LOGGER = LoggingManager.getLogger(ShareCodeGenerator.class);

    private ShareCodeGenerator() {
    }

    private static @NonNull String collapseWhitespace(@NonNull File file) throws FileNotFoundException {
        JsonElement json = JsonParser.parseReader(new FileReader(file));
        return new Gson().toJson(json);
    }

    private static @NonNull String collapseWhitespace(@NonNull String namespacedId) {
        String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);

        File file = PathConstants.getPathOf(namespacedId)
                .resolve(id)
                .toFile();

        try {
            return collapseWhitespace(file);
        } catch (IOException e) {
            LOGGER.error("Error while removing whitespace of {}: {}", file, e);
            return "";
        }
    }

    @Contract("_ -> new")
    private static @NonNull String compressAndBase58Encode(@NonNull String contents) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream)) {
            gzip.write(contents.getBytes(StandardCharsets.UTF_8));
        }

        return new String(BASE_58.encode(byteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
    }

    private static @NonNull String generateCoreShareCode(@NonNull String namespacedId) {
        String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);

        File file = PathConstants.getPathOf(namespacedId)
                .resolve(id)
                .toFile();

        try {
            return compressAndBase58Encode(collapseWhitespace(file));
        } catch (Exception e) {
            LOGGER.error("Error while generating share code for namespaced id mappings: {}", namespacedId, e);
            return "";
        }
    }

    static long genCRC32checksum(@NonNull String contents) {
        byte[] bytes = contents.getBytes(StandardCharsets.UTF_8);
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    public static @NonNull String generate(@NonNull String namespacedId) {
        return "CDS:"
                + "EV" + CmdDeleteClient.SHARECODE_FORMAT_VERSION + ":"
                + generateCoreShareCode(namespacedId) + ":"
                + genCRC32checksum(collapseWhitespace(namespacedId));
    }
}
