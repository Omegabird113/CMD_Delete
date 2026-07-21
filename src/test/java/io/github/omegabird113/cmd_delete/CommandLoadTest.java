package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommandLoadTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void commandRegistrationTest() {
        Assertions.assertDoesNotThrow(NavMappingsCommand::register);
    }
}
