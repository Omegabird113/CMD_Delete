package io.github.omegabird113.cmd_delete;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

final class LoadTimer {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(LoadTimer.class);

    private LoadTimer() {
    }

    static void time(@NonNull Runnable toTime, @NonNull String name, boolean detailed) {
        final long startTime = System.nanoTime();
        toTime.run();
        final long endTime = System.nanoTime();
        final double detailedDuration = (endTime - startTime) / 1000000.0;
        LOGGER.debug("Timed \"{}\". Took exactly {} ms", name, detailedDuration);
        if (!detailed) {
            final long duration = Math.round(detailedDuration);
            LOGGER.info("Timed {}. Took {} ms", name, duration);
        }
    }
}
