package com.unideb.qsa.config.resolver.datasource.configdefinition;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unideb.qsa.config.resolver.datasource.configpack.ConfigPackSource;
import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigKey;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.context.ConfigValue;
import com.unideb.qsa.config.resolver.domain.context.Qualifier;

/**
 * Unit tests for {@link ConfigDefinitionSource}.
 */
public class ConfigDefinitionSourceTest {

    private static final String CONFIG_NAME = "CONFIG_NAME";
    private static final Map<String, String> CONFIG_QUALIFIER = Map.of("name", "configRootName");
    private static final Qualifier QUALIFIER = new Qualifier.Builder().putAll(CONFIG_QUALIFIER).build();
    private static final ConfigValue CONFIG_VALUE_WITH_QUALIFIER = new ConfigValue("valueHu", Map.of("locale", Set.of("hu")));
    private static final ConfigValue CONFIG_VALUE_WITHOUT_QUALIFIER = new ConfigValue("valueDefault", Map.of());
    private static final Set<ConfigValue> CONFIG_VALUES = Set.of(CONFIG_VALUE_WITH_QUALIFIER, CONFIG_VALUE_WITHOUT_QUALIFIER);
    private static final ConfigDefinition CONFIG_DEFINITION = new ConfigDefinition(CONFIG_NAME, CONFIG_VALUES, CONFIG_QUALIFIER);
    private static final int CONTAINS_ONE_ELEMENT = 1;
    private static final int FIRST_ELEMENT = 0;

    @Mock
    private ConfigPackSource configPackSource;

    private ConfigDefinitionSource configDefinitionSource;


    @BeforeMethod
    public void setup() {
        openMocks(this);
        configDefinitionSource = new ConfigDefinitionSource(configPackSource);
    }

    @Test
    public void getConfigPackDefinitions() {
        // GIVEN
        given(configPackSource.getConfigPacks()).willReturn(createTestConfigPacks());
        // WHEN
        var actual = configDefinitionSource.getConfigDefinitions(CONFIG_NAME, QUALIFIER);
        // THEN
        verify(configPackSource).getConfigPacks();
        assertEquals(actual.size(), CONTAINS_ONE_ELEMENT);
        assertEquals(actual.get(FIRST_ELEMENT), CONFIG_DEFINITION);
    }

    private List<ConfigPack> createTestConfigPacks() {
        var configKey = new ConfigKey(CONFIG_DEFINITION);
        return Collections.singletonList(new ConfigPack(Map.of(configKey, CONFIG_DEFINITION)));
    }

}
