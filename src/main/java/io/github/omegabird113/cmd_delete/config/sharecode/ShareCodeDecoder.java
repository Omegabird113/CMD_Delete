package io.github.omegabird113.cmd_delete.config.sharecode;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

import static io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeGenerator.BASE_58;

public final class ShareCodeDecoder {
    private ShareCodeDecoder() {
    }

    @Contract("_ -> new")
    public static @NonNull String decodeCoreShareCode(@NonNull String input) throws IOException {
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(BASE_58.decode(input));
             final GZIPInputStream gzip = new GZIPInputStream(bais)) {
            return new String(gzip.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Contract("_ -> new")
    public static String @NonNull [] getShareCodeStringArray(@NonNull String shareCode) {
        final String[] split = shareCode.split(":");

        if (split.length != 4)
            throw new IllegalArgumentException("Invalid share code (Wrong length): " + shareCode);
        if (!Objects.equals(split[0].toUpperCase(Locale.ROOT), "CDS"))
            throw new IllegalArgumentException("Invalid share code (Not CDS pre-fixed): " + shareCode);
        if (!Objects.equals(split[1].toUpperCase(Locale.ROOT), "EV" + CmdDeleteClient.SHARECODE_FORMAT_VERSION))
            throw new IllegalArgumentException("Invalid share code (Incorrect format version): " + shareCode);

        return split;
    }

    public static @NonNull String decode(@NonNull String shareCode) {
        final String[] split = getShareCodeStringArray(shareCode);

        String coreDecoded;
        try {
            coreDecoded = decodeCoreShareCode(split[2]);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid share code (Failed to decode core): " + shareCode, e);
        }

        final long actualChecksum = ShareCodeGenerator.genCRC32checksum(coreDecoded);
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
