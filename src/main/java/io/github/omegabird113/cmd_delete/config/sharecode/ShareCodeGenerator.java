package io.github.omegabird113.cmd_delete.config.sharecode;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import org.apache.commons.codec.binary.Base58;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.CRC32;
import java.util.zip.GZIPOutputStream;

public final class ShareCodeGenerator {
    static final Base58 BASE_58 = new Base58();
    private static final Logger LOGGER = LoggingManager.getLogger(ShareCodeGenerator.class);

    private ShareCodeGenerator() {}

    private static String collapseWhitespace(File file) throws FileNotFoundException {
        JsonElement json = JsonParser.parseReader(new FileReader(file));
        return new Gson().toJson(json);
    }

    private static String collapseWhitespace(String namespacedId) {
        MappingsState.Type type = MappingsIdResolutionUtils.resolveType(namespacedId);
        String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);

        Path path = type == MappingsState.Type.CUSTOM ? PathConstants.getMappingsJSONPath() : PathConstants.getMappingsResourcePath();
        File file = path.resolve(id + ".json").toFile();

        try {
            return collapseWhitespace(file);
        } catch (IOException e) {
            LOGGER.error("Error while removing whitespace of {}: {}", file, e);
            return "";
        }
    }

    private static String compressAndBase58Encode(String contents) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(contents.getBytes(StandardCharsets.UTF_8));
        }

        return new String(BASE_58.encode(baos.toByteArray()), StandardCharsets.UTF_8);
    }

    private static String generateCoreShareCode(String namespacedId) {
        MappingsState.Type type = MappingsIdResolutionUtils.resolveType(namespacedId);
        String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);

        Path path = type == MappingsState.Type.CUSTOM ? PathConstants.getMappingsJSONPath() : PathConstants.getMappingsResourcePath();
        File file = path.resolve(id + ".json").toFile();

        try {
            return compressAndBase58Encode(collapseWhitespace(file));
        } catch (Exception e) {
            LOGGER.error("Error while generating share code for namespaced id mappings: {}", namespacedId, e);
            return "";
        }
    }

    static long genCRC32checksum(String contents) {
        byte[] bytes = contents.getBytes(StandardCharsets.UTF_8);
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    public static String generate(String namespacedId) {
        return "CDS:"
                + "EV" + CmdDeleteClient.SHARE_CODE_FORMAT_VERSION + ":"
                + generateCoreShareCode(namespacedId) + ":"
                + genCRC32checksum(collapseWhitespace(namespacedId));
    }
}
