package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.actions.ActionOffsetUtils;
import io.github.omegabird113.cmd_delete.actions.NavAction;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.KeyCombo;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.MappingsInheritanceManager;
import io.github.omegabird113.cmd_delete.mappings.NavMappings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;

public class InheritanceTests {
    private static final Logger LOGGER = LoggingManager.getLogger(InheritanceTests.class);

    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void featureFlagsMergeTest() {
        FeatureFlags ff = new FeatureFlags(null, null);
        final FeatureFlags[] featureFlags = new FeatureFlags[TestRandomnessUtils.RANDOM.nextInt(50, 150)];
        for (int i = 0; i < featureFlags.length; i++) {
            featureFlags[i] = TestRandomnessUtils.nextRandFeatureFlags();

            FeatureFlags beforeMergeParent = ff;
            FeatureFlags beforeMergeChild = featureFlags[i];

            ff = FeatureFlags.merge(ff, featureFlags[i]);
            LOGGER.info("{} - Merged {} into {}", i, beforeMergeChild, beforeMergeParent);

            Boolean expected1 = beforeMergeChild.overrideVanillaNavigation() != null
                    ? beforeMergeChild.overrideVanillaNavigation()
                    : beforeMergeParent.overrideVanillaNavigation();
            Assertions.assertEquals(expected1, ff.overrideVanillaNavigation());

            Boolean expected2 = beforeMergeChild.crossLineSignMovement() != null
                    ? beforeMergeChild.crossLineSignMovement()
                    : beforeMergeParent.crossLineSignMovement();
            Assertions.assertEquals(expected2, ff.crossLineSignMovement());
        }
    }

    @Test
    void inheritanceMergeMappingsStressTest() { // HUGE but takes <1 second & is a great stress test
        final int n1 = TestRandomnessUtils.RANDOM.nextInt(4000, 6501);
        final MappingsRegistry[] mappingsRegistries = new MappingsRegistry[n1];
        for (int i = 0; i < n1; i++)
            mappingsRegistries[i] = TestRandomnessUtils.genRandomRegistry();

        LOGGER.info("Generated to-merge list of {} mappings registries", n1);
        final MappingsRegistry mr = MappingsInheritanceManager.merge(List.of(mappingsRegistries));

        NavMappings mappings = new NavMappings(mr);
        LOGGER.info("Merged mappings registries into one of size: {}, hashCode: {}, and with coverage: {}", mr.getSize(), mr.hashCode(), mappings.getCoverage());
        Assertions.assertEquals(1.0f, mappings.getCoverage(), 0.0001f);

        Assertions.assertNotNull(mr);
        Assertions.assertNotNull(mappings);

        Assertions.assertNotNull(mr.name());
        Assertions.assertFalse(mr.name().isBlank());
        Assertions.assertNotNull(mr.version());
        Assertions.assertFalse(mr.version().isBlank());
        Assertions.assertNotNull(mr.description());
        Assertions.assertFalse(mr.description().isBlank());
        Assertions.assertNotNull(mr.author());
        Assertions.assertFalse(mr.author().isBlank());

        Assertions.assertNotNull(mr.featureFlags());
        Assertions.assertNotNull(mr.featureFlags().overrideVanillaNavigation());
        Assertions.assertNotNull(mr.featureFlags().crossLineSignMovement());
        Assertions.assertNotNull(mr.systems());

        final int n2 = TestRandomnessUtils.RANDOM.nextInt(8000, 12001);
        LOGGER.info("Testing {} NavAction lookups in generated mappings", n2);
        for (int i = 0; i < n2; i++) {
            KeyCombo rkey = TestRandomnessUtils.genRandomKeyCombo();
            NavAction na = mappings.getAction(rkey);
            Assertions.assertNotNull(na);
            if (!ActionOffsetUtils.isOverrideAction(Objects.requireNonNullElse(mr.get(rkey), NavAction.NONE)) || mr.featureFlags().overrideVanillaNavigation())
                Assertions.assertEquals(
                        Objects.requireNonNullElse(mr.get(rkey), NavAction.NONE),
                        na,
                        "Expected key " + mr.get(rkey) + " was not provided, instead " + na.name() + ", for " + rkey + " which " + (ActionOffsetUtils.isOverrideAction(na) ? "is" : "is not") + " an override action with override mode " + mr.featureFlags().overrideVanillaNavigation()
                );
            else
                Assertions.assertEquals(NavAction.NONE, na);
        }
    }
}
