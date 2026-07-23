package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.KeyNameRegistry;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Arrays;

public class StringsTest {
    private static final Logger LOGGER = LoggingManager.getLogger(StringsTest.class);

    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void stringsTest() {
        NavMappingsManager.loadMappings();
        Assertions.assertDoesNotThrow(() -> {
            final String[] strings = new String[]{
                    KeyNameRegistry.getDumpString(),
                    NavMappingsManager.getCurrentMappingsRegistry().toString(),
                    NavMappingsManager.getMappingsState().toString(),
                    MappingsInfoCollectionUtils.getInfoFrom(NavMappingsManager.getMappingsState(), true),
                    MappingsInfoCollectionUtils.getInfoFrom(NavMappingsManager.getMappingsState(), false),
                    NavAction.getDetailedActionDump()
            };
            Assertions.assertAll(
                    Arrays.stream(strings)
                            .map(s -> () -> {
                                LOGGER.info("Testing string: {}", s);
                                Assertions.assertFalse(s.isEmpty());
                            })
            );
        });
    }
}
