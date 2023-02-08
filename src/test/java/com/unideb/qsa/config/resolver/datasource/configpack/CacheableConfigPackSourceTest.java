package com.unideb.qsa.config.resolver.datasource.configpack;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * Unit tests for {@link CacheableConfigPackSource}.
 */
public class CacheableConfigPackSourceTest {

    private static final ConfigPack CONFIG_PACK = new ConfigPack(Map.of());

    @Mock
    private ConfigPackSource configPackSource;


    private CacheableConfigPackSource cacheableConfigPackSource;


    @BeforeMethod
    public void setup() {
        openMocks(this);
        cacheableConfigPackSource = new CacheableConfigPackSource(configPackSource, 1);
    }

    @Test
    public void getConfigPacks() {
        // GIVEN
        var expected = List.of(CONFIG_PACK);
        given(configPackSource.getConfigPacks()).willReturn(List.of(CONFIG_PACK));
        // WHEN
        var actual1 = cacheableConfigPackSource.getConfigPacks();
        var actual2 = cacheableConfigPackSource.getConfigPacks();
        // THEN
        verify(configPackSource, times(1)).getConfigPacks();
        assertEquals(actual1, expected);
        assertEquals(actual2, expected);
    }


}
