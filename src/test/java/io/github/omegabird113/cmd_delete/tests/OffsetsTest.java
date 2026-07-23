package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.actions.NavActionOffset;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;

public class OffsetsTest {
    private static final Logger LOGGER = LoggingManager.getLogger(OffsetsTest.class);

    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void offsetsTest() {
        final List<NavAction> allowedInvalidOffsets = List.of(
                NavAction.NONE,
                NavAction.OVR_COPY,
                NavAction.OVR_CUT,
                NavAction.OVR_PASTE,
                NavAction.OVR_SELECT_ALL
        );
        for (NavAction action : NavAction.values()) {
            final int offset = action.offset().value();

            if (action.name().contains("LEFT") && offset != NavActionOffset.LEFT.value())
                Assertions.fail("LEFT offset not produced by action: " + action.name());
            if (action.name().contains("RIGHT") && offset != NavActionOffset.RIGHT.value())
                Assertions.fail("RIGHT offset not produced by action: " + action.name());
            if (action.name().contains("UP") && offset != NavActionOffset.UP.value())
                Assertions.fail("UP offset not produced by action: " + action.name());
            if (action.name().contains("DOWN") && offset != NavActionOffset.DOWN.value())
                Assertions.fail("DOWN offset not produced by action: " + action.name());
            if (!allowedInvalidOffsets.contains(action) && offset == 0)
                Assertions.fail("INVALID offset not produced by action: " + action.name());

            LOGGER.info("Tested offset ({}) of: {}", offset, action.name());

            boolean isOvr = action.overrideMode();
            Assertions.assertEquals(isOvr, action.name().contains("OVR"));

            boolean isMove = action.isMove();
            Assertions.assertEquals(isMove, action.name().contains("NAV"));
        }
    }
}
