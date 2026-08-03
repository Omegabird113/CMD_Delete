package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.config.fileio.PathConstants;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PathConstantsTest {
    @BeforeAll
    static void beforeAll() {
        TestLoader.setup();
    }

    @Test
    void pathConstantsResolvePathsTest() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("mappings", PathConstants.getMappingsResourcePath().getFileName().toString()),
                () -> Assertions.assertEquals(".active_mappings", PathConstants.getActiveMappingsFilePath().getFileName().toString()),
                () -> Assertions.assertEquals("mappings", PathConstants.getMappingsJSONPath().getFileName().toString()),
                () -> Assertions.assertEquals(PathConstants.getMappingsResourcePath(), PathConstants.getPathOf(MappingsType.BUILTIN, "mac").getParent()),
                () -> Assertions.assertEquals(PathConstants.getMappingsJSONPath(), PathConstants.getPathOf(MappingsType.CUSTOM, "sample").getParent()),
                () -> Assertions.assertEquals("mac.json", PathConstants.getPathOf(MappingsType.BUILTIN, "mac").getFileName().toString()),
                () -> Assertions.assertEquals("sample.json", PathConstants.getPathOf(MappingsType.CUSTOM, "sample").getFileName().toString()),
                () -> Assertions.assertEquals("default.json", PathConstants.getPathOf("default").getFileName().toString())
        );
    }
}
