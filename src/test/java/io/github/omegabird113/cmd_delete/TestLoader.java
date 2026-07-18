package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.command.NavMappingsCommand;
import io.github.omegabird113.cmd_delete.config.data.*;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsInheritanceManager;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeDecoder;
import io.github.omegabird113.cmd_delete.config.sharecode.ShareCodeGenerator;
import io.github.omegabird113.cmd_delete.mappings.MappingsState;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import io.github.omegabird113.cmd_delete.mappings.Os;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

public class TestLoader {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(TestLoader.class);
    private static final @NonNull Random  RANDOM = new Random();
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
    void allBuiltinMappingsLoadTest() {
        NavMappingsManager.loadMappings();
        MappingsState lastState = NavMappingsManager.getMappingsState();
        for (final String namespacedId : MappingsInfoCollectionUtils.getMappingsList()) {
            if (namespacedId.equals("default") || namespacedId.startsWith("custom:"))
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
            final int offset = ActionOffsetUtils.getOffset(action);

            if (action.name().contains("LEFT") && offset != ActionOffsetUtils.OFFSET_LEFT)
                Assertions.fail("LEFT offset not produced by action: " + action.name());
            if (action.name().contains("RIGHT") && offset != ActionOffsetUtils.OFFSET_RIGHT)
                Assertions.fail("RIGHT offset not produced by action: " + action.name());
            if (action.name().contains("UP") && offset != ActionOffsetUtils.OFFSET_UP)
                Assertions.fail("UP offset not produced by action: " + action.name());
            if (action.name().contains("DOWN") && offset != ActionOffsetUtils.OFFSET_DOWN)
                Assertions.fail("DOWN offset not produced by action: " + action.name());
            if (!allowedInvalidOffsets.contains(action) && offset == 0)
                Assertions.fail("INVALID offset not produced by action: " + action.name());

            LOGGER.info("Tested offset ({}) of: {}", offset, action.name());

            boolean isOvr = ActionOffsetUtils.isOverrideAction(action);
            Assertions.assertEquals(isOvr, action.name().contains("OVR"));

            boolean isMove = ActionOffsetUtils.isMoveAction(action);
            Assertions.assertEquals(isMove, action.name().contains("NAV"));
        }
    }

    private @Nullable Boolean nextRandNullableBoolean() {
        int choose = RANDOM.nextInt(0, 3);
        if (choose == 0)
            return null;
        if (choose == 1)
            return Boolean.FALSE;
        if (choose == 2)
            return Boolean.TRUE;
        return null;
    }

    private FeatureFlags nextRandFeatureFlags() {
        final Boolean overrideVanillaNavigation = nextRandNullableBoolean();
        final Boolean crossLineSignMovement = nextRandNullableBoolean();
        return new FeatureFlags(overrideVanillaNavigation, crossLineSignMovement);
    }

    @Test
    void featureFlagsMergeTest() {
        FeatureFlags ff = new FeatureFlags(null, null);
        final FeatureFlags[] featureFlags = new FeatureFlags[RANDOM.nextInt(100, 201)];
        for (int i = 0; i < featureFlags.length; i++) {
            featureFlags[i] = nextRandFeatureFlags();
            ff = FeatureFlags.merge(ff, featureFlags[i]);
            LOGGER.info("{} - Merged {} into {}", i, featureFlags[i], ff);
        }
    }

    private KeyCombo genRandomKeyCombo() {
        final boolean shift = RANDOM.nextBoolean();
        final boolean ctrl = RANDOM.nextBoolean();
        final boolean altOption = RANDOM.nextBoolean();
        final boolean superCommand = RANDOM.nextBoolean();
        final int key = RANDOM.nextInt(0, 8096);
        return new KeyCombo(key, shift, altOption, ctrl, superCommand);
    }

    private String genRandomString(int length) {
        final byte[] strBytes = new byte[length];
        RANDOM.nextBytes(strBytes);
        return new String(strBytes, StandardCharsets.UTF_8);
    }

    private List<Os> genSystems() {
        final boolean windows = RANDOM.nextBoolean();
        final boolean linux = RANDOM.nextBoolean();
        final boolean mac = RANDOM.nextBoolean();
        final List<Os> systems = new ArrayList<>();
        if (windows)
            systems.add(Os.WINDOWS);
        if (linux)
            systems.add(Os.LINUX);
        if (mac)
            systems.add(Os.MAC);
        return systems;
    }

    private MappingsRegistry genRandomRegistry() {
        final Map<KeyCombo, NavAction> enabled = new HashMap<>();
        final Map<KeyCombo, NavAction> disabled = new HashMap<>();
        final NavAction[] NavActions = Arrays.stream(NavAction.values()).toArray(NavAction[]::new);

        final int n1 = RANDOM.nextInt(30, 1001);
        for (int i = 0; i < n1; i++) {
            KeyCombo key = genRandomKeyCombo();
            NavAction na = NavActions[RANDOM.nextInt(0, NavActions.length)];
            enabled.put(key, na);
        }

        final int n2 = RANDOM.nextInt(30, 1001);
        for (int i = 0; i < n2; i++) {
            KeyCombo key = genRandomKeyCombo();
            NavAction na = NavActions[RANDOM.nextInt(0, NavActions.length)];
            disabled.put(key, na);
        }

        final FeatureFlags ff = nextRandFeatureFlags();
        final List<Os> systems = genSystems();

        final String name = genRandomString(RANDOM.nextInt(8, 25));
        final String description = genRandomString(RANDOM.nextInt(50, 1001));
        final String id = genRandomString(RANDOM.nextInt(6, 15));
        final String author = genRandomString(RANDOM.nextInt(7, 35));
        final String inherits = genRandomString(RANDOM.nextInt(6, 15));
        final String version = "" + RANDOM.nextLong(0, Long.MAX_VALUE);

        return new MappingsRegistry(enabled, disabled, systems, ff, inherits, name, author, description, version, id);
    }

    private static @NonNull String gzipAndBase64Encode(@NonNull String contents) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Base64.Encoder encoder = Base64.getEncoder();

        try (GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream)) {
            gzip.write(contents.getBytes(StandardCharsets.UTF_8));
        }

        return new String(encoder.encode(byteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
    }

    @Test
    void inheritanceMergeTest() {
        final int n = RANDOM.nextInt(3000, 8001);
        final MappingsRegistry[] mappingsRegistries = new MappingsRegistry[n];
        for (int i = 0; i < n; i++)
            mappingsRegistries[i] = genRandomRegistry();
        LOGGER.info("Generated to-merge list of {} mappings registries", n);
        final MappingsRegistry mr = MappingsInheritanceManager.merge(List.of(mappingsRegistries));
        LOGGER.info("Merged registries. Generating base64-encoded gzip-ed string...");
        String encodedMrStr = "";
        try {
            encodedMrStr = gzipAndBase64Encode(mr.toString());
        } catch (IOException e) {
            Assertions.fail(e);
        }
        LOGGER.info("Merged mappings registries into: \"{}\"", encodedMrStr);
    }
}
