/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.utils;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

public final class LoadTimer {
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(LoadTimer.class);

    private LoadTimer() {
    }

    public static void time(final @NonNull Runnable toTime, final @NonNull String name, final boolean detailed) {
        final long startTime = System.nanoTime();
        try {
            toTime.run();
        } finally {
            final long endTime = System.nanoTime();
            final double detailedDuration = (endTime - startTime) / 1000000.0;
            LoggingManager.traceLog(LOGGER, "Timed \"{}\". Took exactly {} ms", name, detailedDuration);
            if (!detailed) {
                final long duration = Math.round(detailedDuration);
                LOGGER.info("Timed {}. Took {} ms", name, duration);
            }
        }
    }
}
