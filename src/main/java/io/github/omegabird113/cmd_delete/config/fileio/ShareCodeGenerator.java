/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.config.fileio;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import org.apache.commons.codec.binary.Base58;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ShareCodeGenerator {
    public static final @NonNull Base58 BASE_58 = new Base58();
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(ShareCodeGenerator.class);

    private ShareCodeGenerator() {
    }

    public static @NonNull String collapseWhitespace(final @NonNull Path path) throws IOException {
        try (final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            final JsonElement json = JsonParser.parseReader(reader);
            return MappingsJSONManager.GSON.toJson(json);
        }
    }

    public static @NonNull String collapseWhitespace(final @NonNull String namespacedId) {
        final Path path = PathConstants.getPathOf(namespacedId);
        try {
            return collapseWhitespace(path);
        } catch (IOException e) {
            LOGGER.error("Error while removing whitespace of {}: {}", path, e);
            return "";
        }
    }

    @Contract("_ -> new")
    public static @NonNull String compressAndBase58Encode(final @NonNull String contents) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream)) {
            gzip.write(contents.getBytes(StandardCharsets.UTF_8));
        }
        return new String(BASE_58.encode(byteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
    }

    public static @NonNull String generateCoreShareCode(final @NonNull String namespacedId) {
        final Path path = PathConstants.getPathOf(namespacedId);
        try {
            return compressAndBase58Encode(collapseWhitespace(path));
        } catch (Exception e) {
            LOGGER.error("Error while generating share code for namespaced id mappings: {}", namespacedId, e);
            return "";
        }
    }

    public static long genCRC32checksum(final @NonNull String contents) {
        byte[] bytes = contents.getBytes(StandardCharsets.UTF_8);
        final CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    public static @NonNull String encode(final @NonNull String namespacedId) {
        return "CDS:"
                + "EV" + CmdDeleteClient.SHARECODE_FORMAT_VERSION + ":"
                + generateCoreShareCode(namespacedId) + ":"
                + genCRC32checksum(collapseWhitespace(namespacedId));
    }

    @Contract("_ -> new")
    public static @NonNull String decodeCoreShareCode(final @NonNull String input) throws IOException {
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(BASE_58.decode(input));
             final GZIPInputStream gzip = new GZIPInputStream(bais)) {
            return new String(gzip.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Contract("_ -> new")
    public static String @NonNull [] getShareCodeStringArray(final @NonNull String shareCode) {
        final String[] split = shareCode.split(":");

        if (split.length != 4)
            throw new IllegalArgumentException("Invalid share code (Wrong length): " + shareCode);
        if (!Objects.equals(split[0].toUpperCase(Locale.ROOT), "CDS"))
            throw new IllegalArgumentException("Invalid share code (Not CDS pre-fixed): " + shareCode);
        if (!Objects.equals(split[1].toUpperCase(Locale.ROOT), "EV" + CmdDeleteClient.SHARECODE_FORMAT_VERSION))
            throw new IllegalArgumentException("Invalid share code (Incorrect format version): " + shareCode);

        return split;
    }

    public static @NonNull String decode(final @NonNull String shareCode) {
        final String[] split = getShareCodeStringArray(shareCode);

        String coreDecoded;
        try {
            coreDecoded = decodeCoreShareCode(split[2]);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid share code (Failed to decode core): " + shareCode, e);
        }

        final long actualChecksum = genCRC32checksum(coreDecoded);
        long expectedChecksum;
        try {
            expectedChecksum = Long.parseLong(split[3]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid share code (Invalid checksum): " + shareCode, e);
        }

        if (actualChecksum != expectedChecksum) {
            throw new IllegalArgumentException("Invalid share code (Checksum mismatch): " + shareCode);
        }

        return coreDecoded;
    }
}
