package com.unideb.qsa.config.resolver.datasource.configpack;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.unideb.qsa.domain.context.ConfigPack;
import com.unideb.qsa.domain.exception.ConfigPackException;

/**
 * Unit tests for {@link FileConfigPackSource}.
 */
public class FileConfigPackSourceTest {

    private static final ConfigPack CONFIG_PACK = new ConfigPack(Map.of());
    private static final String VALID_PATH = "src/test/resources";
    private static final String VALID_URL = "https://data/config2";
    private static final List<String> CONFIG_URIS = List.of(VALID_PATH, VALID_URL);
    private static final String INVALID_PATH = "src/test";

    private FileConfigPackSource fileConfigPackSource;

    @Test
    public void getConfigPacks() {
        // GIVEN
        List<ConfigPack> expected = List.of(CONFIG_PACK);
        fileConfigPackSource = new FileConfigPackSource(CONFIG_URIS);
        // WHEN
        List<ConfigPack> actual = fileConfigPackSource.getConfigPacks();
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
