package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
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
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(TestLoader.class);
    private static final Path tempDir;
    private static boolean initialized = false;

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
            PathConstants.init(
                    tempDir,
                    Path.of(Objects.requireNonNull(CmdDeleteClient.class.getResource("/mappings")).toURI())
            );
            initialized = true;

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
    }

    @AfterAll
    static void afterAll() {
        try {


            Files.deleteIfExists(tempDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
