package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.utils.LoadTimer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoadTimerTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void timeRunnableRunTest() {
        Assertions.assertDoesNotThrow(() -> LoadTimer.time(() -> {
            int n = 0;
            while (n < 100) {
                n++;
            }
        }, "loop", true));
    }
}
