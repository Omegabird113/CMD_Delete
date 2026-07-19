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
        final FeatureFlags[] featureFlags = new FeatureFlags[TestRandomnessUtils.RANDOM.nextInt(100, 201)];
        for (int i = 0; i < featureFlags.length; i++) {
            featureFlags[i] = TestRandomnessUtils.nextRandFeatureFlags();

            FeatureFlags beforeMergeParent = ff;
            FeatureFlags beforeMergeChild = featureFlags[i];

            ff = FeatureFlags.merge(ff, featureFlags[i]);
            LOGGER.info("{} - Merged {} into {}", i, beforeMergeChild, beforeMergeParent);

            if (beforeMergeParent.overrideVanillaNavigation() == null)
                Assertions.assertEquals(ff.overrideVanillaNavigation(), beforeMergeChild.overrideVanillaNavigation());
            else if (beforeMergeChild.overrideVanillaNavigation() == null)
                Assertions.assertEquals(ff.overrideVanillaNavigation(), beforeMergeParent.overrideVanillaNavigation());
            else
                Assertions.assertEquals(ff.overrideVanillaNavigation(), beforeMergeChild.overrideVanillaNavigation());

            if (beforeMergeParent.crossLineSignMovement() == null)
                Assertions.assertEquals(ff.crossLineSignMovement(), beforeMergeChild.crossLineSignMovement());
            else if (beforeMergeChild.crossLineSignMovement() == null)
                Assertions.assertEquals(ff.crossLineSignMovement(), beforeMergeParent.crossLineSignMovement());
            else
                Assertions.assertEquals(ff.crossLineSignMovement(), beforeMergeChild.crossLineSignMovement());
        }
    }

    @Test
    void inheritanceMergeMappingsStressTest() {
        final int n1 = TestRandomnessUtils.RANDOM.nextInt(4000, 6501);
        final MappingsRegistry[] mappingsRegistries = new MappingsRegistry[n1];
        for (int i = 0; i < n1; i++)
            mappingsRegistries[i] = TestRandomnessUtils.genRandomRegistry();

        LOGGER.info("Generated to-merge list of {} mappings registries", n1);
        final MappingsRegistry mr = MappingsInheritanceManager.merge(List.of(mappingsRegistries));

        NavMappings mappings = new NavMappings(mr);
        LOGGER.info("Merged mappings registries into one of size: {}, hashCode: {}, and with coverage: {}", mr.getSize(), mr.hashCode(), mappings.getCoverage());
        Assertions.assertEquals(1.0f, mappings.getCoverage());

        Assertions.assertNotNull(mr);
        Assertions.assertNotNull(mappings);

        Assertions.assertNotNull(mr.name());
        Assertions.assertNotEquals("", mr.name());
        Assertions.assertNotNull(mr.version());
        Assertions.assertNotEquals("", mr.version());
        Assertions.assertNotNull(mr.description());
        Assertions.assertNotEquals("", mr.description());
        Assertions.assertNotNull(mr.author());
        Assertions.assertNotEquals("", mr.author());

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
