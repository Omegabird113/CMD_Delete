package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.TestLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CmdDeleteClientConstantsTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void clientConstantsTest() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("cmd_delete", CmdDeleteClient.MODID),
                () -> Assertions.assertTrue(CmdDeleteClient.ISSUE_TRACKER_URL_STRING.startsWith("https://github.com/")),
                () -> Assertions.assertTrue(CmdDeleteClient.CURRENT_MAPPINGS_FORMAT_VERSION >= CmdDeleteClient.MINIMUM_MAPPINGS_FORMAT_VERSION),
                () -> Assertions.assertNotNull(CmdDeleteClient.VERSION),
                () -> Assertions.assertFalse(CmdDeleteClient.VERSION.isBlank()),
                () -> Assertions.assertNotEquals("<unknown>", CmdDeleteClient.VERSION)
        );
    }
}
