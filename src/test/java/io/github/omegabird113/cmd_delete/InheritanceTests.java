package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsInheritanceManager;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class InheritanceTests {
    private static final Logger LOGGER = LoggingManager.getLogger(InheritanceTests.class);

    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    static @NonNull String gzipAndBase64Encode(@NonNull String contents) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Base64.Encoder encoder = Base64.getEncoder();

        try (GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream)) {
            gzip.write(contents.getBytes(StandardCharsets.UTF_8));
        }

        return new String(encoder.encode(byteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
    }

    @Test
    void featureFlagsMergeTest() {
        FeatureFlags ff = new FeatureFlags(null, null);
        final FeatureFlags[] featureFlags = new FeatureFlags[TestRandomnessUtils.RANDOM.nextInt(100, 201)];
        for (int i = 0; i < featureFlags.length; i++) {
            featureFlags[i] = TestRandomnessUtils.nextRandFeatureFlags();

            FeatureFlags beforeMergeParent = ff;
            FeatureFlags beforeMergeChild = featureFlags[i];

            ff = FeatureFlags.merge(ff, featureFlags[i]);
            LOGGER.info("{} - Merged {} into {}", i, beforeMergeChild, beforeMergeParent);

            if (beforeMergeParent.overrideVanillaNavigation() == null)
                Assertions.assertEquals(ff.overrideVanillaNavigation(), beforeMergeChild.overrideVanillaNavigation());
            else
                if (beforeMergeChild.overrideVanillaNavigation() == null)
                    Assertions.assertEquals(ff.overrideVanillaNavigation(), beforeMergeParent.overrideVanillaNavigation());
                else
                    Assertions.assertEquals(ff.overrideVanillaNavigation(), beforeMergeChild.overrideVanillaNavigation());

            if (beforeMergeParent.crossLineSignMovement() == null)
                Assertions.assertEquals(ff.crossLineSignMovement(), beforeMergeChild.crossLineSignMovement());
            else
            if (beforeMergeChild.crossLineSignMovement() == null)
                Assertions.assertEquals(ff.crossLineSignMovement(), beforeMergeParent.crossLineSignMovement());
            else
                Assertions.assertEquals(ff.crossLineSignMovement(), beforeMergeChild.crossLineSignMovement());
        }
    }

    @Test
    void inheritanceMergeTest() {
        final int n = TestRandomnessUtils.RANDOM.nextInt(3000, 8001);
        final MappingsRegistry[] mappingsRegistries = new MappingsRegistry[n];
        for (int i = 0; i < n; i++)
            mappingsRegistries[i] = TestRandomnessUtils.genRandomRegistry();
        LOGGER.info("Generated to-merge list of {} mappings registries", n);
        final MappingsRegistry mr = MappingsInheritanceManager.merge(List.of(mappingsRegistries));
        LOGGER.info("Merged registries. Generating base64-encoded gzip-ed string...");
        String encodedMrStr = "";
        try {
            encodedMrStr = gzipAndBase64Encode(mr.toString());
        } catch (IOException e) {
            Assertions.fail(e);
        }
        LOGGER.info("Merged mappings registries into: \"{}\"", encodedMrStr);

        Assertions.assertNotNull(mr);

        Assertions.assertNotNull(mr.name());
        Assertions.assertNotEquals("", mr.name());
        Assertions.assertNotNull(mr.version());
        Assertions.assertNotEquals("", mr.version());
        Assertions.assertNotNull(mr.description());
        Assertions.assertNotEquals("", mr.description());
        Assertions.assertNotNull(mr.author());
        Assertions.assertNotEquals("", mr.author());

        Assertions.assertNotNull(mr.featureFlags());
        Assertions.assertNotNull(mr.featureFlags().overrideVanillaNavigation());
        Assertions.assertNotNull(mr.featureFlags().crossLineSignMovement());
        Assertions.assertNotNull(mr.systems());
    }
}
