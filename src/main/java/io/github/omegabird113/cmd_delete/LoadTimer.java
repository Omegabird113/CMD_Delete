package io.github.omegabird113.cmd_delete;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class LoadTimer {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(LoadTimer.class);

    static void time(@NonNull Runnable toTime, @NonNull String name, boolean detailed) {
        final long startTime = System.nanoTime();
        toTime.run();
        final long endTime = System.nanoTime();
        if (detailed) {
            final double duration = (endTime - startTime) / 1000000.0f;
            LOGGER.debug("Timed {}. Took {} ms", name, duration);
        } else {
            final long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            LOGGER.info("Timed {}. Took {} ms", name, duration);
        }
    }
}
