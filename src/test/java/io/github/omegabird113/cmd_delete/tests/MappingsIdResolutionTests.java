package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MappingsIdResolutionTests {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void idResolutionTest() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("custom:sample", MappingsIdResolutionUtils.resolveNamespacedId(MappingsType.CUSTOM, "sample")),
                () -> Assertions.assertEquals("builtin:mac", MappingsIdResolutionUtils.resolveNamespacedId(MappingsType.BUILTIN, "mac")),
                () -> Assertions.assertEquals("", MappingsIdResolutionUtils.resolveNamespacedId(MappingsType.DEFAULT, ""))
        );
    }

    @Test
    void resolveTypeAndRemoveNamespaceTest() {
        Assertions.assertAll(
                () -> Assertions.assertEquals(MappingsType.CUSTOM, MappingsIdResolutionUtils.resolveType("custom:sample")),
                () -> Assertions.assertEquals(MappingsType.BUILTIN, MappingsIdResolutionUtils.resolveType("builtin:mac")),
                () -> Assertions.assertEquals(MappingsType.DEFAULT, MappingsIdResolutionUtils.resolveType("default")),
                () -> Assertions.assertEquals(MappingsType.DEFAULT, MappingsIdResolutionUtils.resolveType("unexpected")),
                () -> Assertions.assertEquals(MappingsType.DEFAULT, MappingsIdResolutionUtils.resolveType("")),
                () -> Assertions.assertEquals("sample", MappingsIdResolutionUtils.removeNamespaceFromId("custom:sample")),
                () -> Assertions.assertEquals("mac", MappingsIdResolutionUtils.removeNamespaceFromId("builtin:mac")),
                () -> Assertions.assertEquals("default", MappingsIdResolutionUtils.removeNamespaceFromId("default")),
                () -> Assertions.assertEquals("unexpected", MappingsIdResolutionUtils.removeNamespaceFromId("unexpected")),
                () -> Assertions.assertEquals("", MappingsIdResolutionUtils.removeNamespaceFromId(""))
        );
    }
}
