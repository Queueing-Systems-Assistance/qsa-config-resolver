package com.unideb.qsa.config.resolver.resolver;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.unideb.qsa.config.resolver.datasource.configdefinition.ConfigDefinitionSource;
import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigValue;
import com.unideb.qsa.config.resolver.domain.context.Qualifier;

/**
 * Unit tests for {@link DefaultConfigResolver}.
 */
@Listeners(MockitoTestNGListener.class)
public class DefaultConfigResolverTest {

    private static final String CONFIG_NAME = "CONFIG_NAME";
    private static final Qualifier QUALIFIER = new Qualifier.Builder().build();
    private static final ConfigValue CONFIG_VALUE_WITH_QUALIFIER = new ConfigValue("valueHu", Map.of("locale", Set.of("hu")));
    private static final ConfigValue CONFIG_VALUE_WITHOUT_QUALIFIER = new ConfigValue("valueDefault", Map.of());
    private static final Set<ConfigValue> CONFIG_VALUES = Set.of(CONFIG_VALUE_WITH_QUALIFIER, CONFIG_VALUE_WITHOUT_QUALIFIER);
    private static final ConfigDefinition CONFIG_DEFINITION = new ConfigDefinition(CONFIG_NAME, CONFIG_VALUES, Map.of("name", "configRootName"));

    @Mock
    private ConfigDefinitionSource configDefinitionSource;

    private DefaultConfigResolver defaultConfigResolver;

    @BeforeMethod
    public void setup() {
        defaultConfigResolver = new DefaultConfigResolver(configDefinitionSource);
    }

    @Test
    public void resolve() {
        // GIVEN
        var expected = Optional.of("valueDefault");
        given(configDefinitionSource.getConfigDefinitions(CONFIG_NAME, QUALIFIER)).willReturn(List.of(CONFIG_DEFINITION));
        // WHEN
        var actual = defaultConfigResolver.resolve(CONFIG_NAME, QUALIFIER);
        // THEN
        verify(configDefinitionSource).getConfigDefinitions(CONFIG_NAME, QUALIFIER);
        assertEquals(actual, expected);
    }

    @Test
    public void resolveShouldReturnEmptyOptionalWhenConfigNotFound() {
        // GIVEN
        var expected = Optional.empty();
        given(configDefinitionSource.getConfigDefinitions(CONFIG_NAME, QUALIFIER)).willReturn(List.of());
        // WHEN
        var actual = defaultConfigResolver.resolve(CONFIG_NAME, QUALIFIER);
        // THEN
        verify(configDefinitionSource).getConfigDefinitions(CONFIG_NAME, QUALIFIER);
        assertEquals(actual, expected);
    }
}
