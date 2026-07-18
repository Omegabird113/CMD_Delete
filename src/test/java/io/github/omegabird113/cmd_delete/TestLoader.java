package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.data.KeyCodeRegistry;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeDecoder;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeGenerator;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class TestLoader {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(TestLoader.class);
    private static @TempDir Path tempDir;

    @BeforeAll
    static void beforeAll() {
        Assertions.assertDoesNotThrow(() -> {
            PathConstants.init(
                    tempDir,
                    Path.of(Objects.requireNonNull(CmdDeleteClient.class.getResource("/mappings")).toURI())
            );

            LOGGER.info("Temp directory is {}", tempDir.toString());

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

    @Test
    void mappingsLoadTest() {
        NavMappingsManager.loadMappings();
        Assertions.assertDoesNotThrow(() -> {
            NavMappingsManager.getMappingsState();
        });
    }

    @Test
    void allMappingsLoadTest() {
        NavMappingsManager.loadMappings();
        MappingsState lastState = NavMappingsManager.getMappingsState();
        for (final String namespacedId : MappingsInfoCollectionUtils.getMappingsList()) {
            if (namespacedId.equals("default"))
                continue;
            final String id = MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId);
            final MappingsState.Type type = id.isEmpty() ? MappingsState.Type.DEFAULT : MappingsIdResolutionUtils.resolveType(namespacedId);
            switch (type) {
                case CUSTOM -> Assertions.assertTrue(NavMappingsManager.updateMappingsToCustom(id));
                case BUILTIN -> Assertions.assertTrue(NavMappingsManager.updateMappingsToBuiltIn(id));
            }
            final MappingsState current = NavMappingsManager.getMappingsState();
            Assertions.assertNotEquals(lastState, current, () -> "Mappings failed to load: " + namespacedId);
            lastState = current;
        }
    }

    @Test
    void allSharecodesGenerateAndDecodeTest() {
        for (final String namespacedId : MappingsInfoCollectionUtils.getMappingsList()) {
            if (namespacedId.equals("default"))
                continue;
            Assertions.assertDoesNotThrow(() -> {
                final String s = ShareCodeGenerator.generate(namespacedId);
                if (s.equals("CDS:EV1::0"))
                    Assertions.fail("Blank sharecode generated for mappings: " + namespacedId);
                final String d = ShareCodeDecoder.decode(s);
                Assertions.assertEquals(d, ShareCodeGenerator.collapseWhitespace(PathConstants.getPathOf(namespacedId).toFile()));
                LOGGER.info("Sharecode of \"{}\" is \"{}\" decoded to \"{}\"", namespacedId, s, d);
            });
        }
    }

    @Test
    void sampleLoadTest() {
        NavMappingsManager.loadMappings();
        final MappingsState before = NavMappingsManager.getMappingsState();
        boolean success = NavMappingsManager.updateMappingsToCustom("sample");
        final MappingsState after = NavMappingsManager.getMappingsState();
        Assertions.assertNotEquals(before, after, "sample mappings failed to load");
        Assertions.assertTrue(success);
    }

    @Test
    void commandRegistrationTest() {
        Assertions.assertDoesNotThrow(NavMappingsCommand::register);
    }

    @Test
    void stringsTest() {
        NavMappingsManager.loadMappings();
        Assertions.assertDoesNotThrow(() -> {
            final String[] strings = new String[]{
                    KeyCodeRegistry.getDumpString(),
                    NavMappingsManager.getCurrentMappingsRegistry().toString(),
                    NavMappingsManager.getMappingsState().toString(),
                    MappingsInfoCollectionUtils.getInfoFrom(NavMappingsManager.getMappingsState(), true),
                    MappingsInfoCollectionUtils.getInfoFrom(NavMappingsManager.getMappingsState(), false)
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
