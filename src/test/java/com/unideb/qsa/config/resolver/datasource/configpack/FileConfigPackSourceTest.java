package com.unideb.qsa.config.resolver.datasource.configpack;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.exception.ConfigPackException;

/**
 * Unit tests for {@link FileConfigPackSource}.
 */
public class FileConfigPackSourceTest {

    private static final ConfigPack CONFIG_PACK = new ConfigPack(Map.of());
    private static final String VALID_PATH = "src/test/resources/test-config-pack.json";
    private static final List<String> CONFIG_URIS = List.of(VALID_PATH);
    private static final String INVALID_PATH = "src/test";

    private FileConfigPackSource fileConfigPackSource;

    @Test
    public void getConfigPacks() {
        // GIVEN
        var expected = List.of(CONFIG_PACK);
        fileConfigPackSource = new FileConfigPackSource(CONFIG_URIS);
        // WHEN
        var actual = fileConfigPackSource.getConfigPacks();
        // THEN
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = ConfigPackException.class)
    public void getConfigPacksShouldThrowExceptionWhenFileErrorOccurs() {
        // GIVEN
        fileConfigPackSource = new FileConfigPackSource(List.of(INVALID_PATH));
        // WHEN
        fileConfigPackSource.getConfigPacks();
        // THEN
    }

}
