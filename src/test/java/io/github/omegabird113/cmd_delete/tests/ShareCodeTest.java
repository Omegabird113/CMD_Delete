package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeDecoder;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeGenerator;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class ShareCodeTest {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(ShareCodeTest.class);

    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void allSharecodesGenerateAndDecodeTest() {
        for (final String namespacedId : MappingsInfoCollectionUtils.getMappingsList()) {
            if (namespacedId.equals("default"))
                continue;
            Assertions.assertDoesNotThrow(() -> {
                final String s = ShareCodeGenerator.generate(namespacedId);
                if (s.equals("CDS:EV1::0"))
                    Assertions.fail("Blank sharecode generated for mappings: " + namespacedId);
                final String d = ShareCodeDecoder.decode(s);
                Assertions.assertEquals(d, ShareCodeGenerator.collapseWhitespace(PathConstants.getPathOf(namespacedId).toFile()));
                LOGGER.info("Sharecode of \"{}\" is \"{}\" decoded to \"{}\"", namespacedId, s, d);
            });
        }
    }
}
