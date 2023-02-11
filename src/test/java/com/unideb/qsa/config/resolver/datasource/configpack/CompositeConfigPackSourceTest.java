package com.unideb.qsa.config.resolver.datasource.configpack;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * Unit tests for {@link CompositeConfigPackSource}.
 */
@Listeners(MockitoTestNGListener.class)
public class CompositeConfigPackSourceTest {

    private static final ConfigPack CONFIG_PACK_1 = new ConfigPack(Map.of());
    private static final ConfigPack CONFIG_PACK_2 = new ConfigPack(Map.of());

    @Mock
    private ConfigPackSource configPackSource1;
    @Mock
    private ConfigPackSource configPackSource2;

    private CompositeConfigPackSource compositeConfigPackSource;


    @BeforeMethod
    public void setup() {
        compositeConfigPackSource = new CompositeConfigPackSource(List.of(configPackSource1, configPackSource2));
    }

    @Test
    public void getConfigPacks() {
        // GIVEN
        var expected = List.of(CONFIG_PACK_1, CONFIG_PACK_2);
        given(configPackSource1.getConfigPacks()).willReturn(Collections.singletonList(CONFIG_PACK_1));
        given(configPackSource2.getConfigPacks()).willReturn(Collections.singletonList(CONFIG_PACK_2));
        // WHEN
        var actual = compositeConfigPackSource.getConfigPacks();
        // THEN
        verify(configPackSource1).getConfigPacks();
        verify(configPackSource2).getConfigPacks();
        assertEquals(actual, expected);
    }

}
