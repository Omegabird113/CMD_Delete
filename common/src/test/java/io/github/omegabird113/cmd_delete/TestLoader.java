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

package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

public class TestLoader {
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(TestLoader.class);
    private static final Path tempDir;
    private static boolean initialized = false;

    private static final IPlatform TEST_PLATFORM = new IPlatform() {
        @Override
        public @NonNull String getModVersion() {
            return "test";
        }

        @Override
        public @NonNull Path getGamePath() {
            return tempDir;
        }

        @Override
        public @NonNull Path getResourcePath() {
            return Path.of(Objects.requireNonNull(TestLoader.class.getResource("/mappings")).getPath());
        }

        @Override
        public <S extends SharedSuggestionProvider> void registerClientCommand(@NonNull CommandRegistration<S> registration) {}
    };

    static {
        try {
            tempDir = Files.createTempDirectory("cmd_delete_tests");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static synchronized void setup() {
        if (initialized)
            return;
        Assertions.assertDoesNotThrow(() -> {
            new CmdDeleteClient(TEST_PLATFORM);
            PathConstants.init(
                    tempDir,
                    Path.of(Objects.requireNonNull(CmdDeleteClient.class.getResource("/mappings")).toURI())
            );
            LOGGER.info("Temp directory is {}", tempDir);
            try (Stream<Path> fis = Files.walk(Path.of(Objects.requireNonNull(TestLoader.class.getResource("/test_mappings")).toURI()))) {
                fis.filter(Files::isRegularFile).forEach((path) -> {
                    try {
                        Files.copy(path, tempDir.resolve("config/cmd_delete/mappings").resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
        initialized = true;
    }

    @AfterAll
    public static void afterAll() {
        try {
            Files.deleteIfExists(tempDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
