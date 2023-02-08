package com.unideb.qsa.config.resolver.domain.deserializer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigKey;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.context.ConfigValue;

/**
 * Unit tests for {@link ConfigPackDeserializer}.
 */
public class ConfigPackDeserializerTest {

    private static final ConfigValue CONFIG_VALUE_WITH_QUALIFIER = new ConfigValue("valueHu", Map.of("locale", Set.of("hu")));
    private static final ConfigValue CONFIG_VALUE_WITHOUT_QUALIFIER = new ConfigValue("valueDefault", Map.of());
    private static final ConfigDefinition CONFIG_DEFINITION = new ConfigDefinition("CONFIG_NAME",
            Set.of(CONFIG_VALUE_WITH_QUALIFIER, CONFIG_VALUE_WITHOUT_QUALIFIER), Map.of("name", "configRootName"));
    private static final Type TYPE_TOKEN = new TypeToken<ConfigPack>() {}.getType();
    private static final Gson GSON = new Gson();
    private static final JsonElement JSON_DATA = GSON.fromJson(
            "{\"config\":[{\"config\":\"CONFIG_NAME\",\"name\":\"configRootName\",\"configCondition\":[\"locale\"],\"values\":"
            + "[{\"value\":\"valueHu\",\"locale\":[\"hu\"]},{\"value\":\"valueDefault\"}]}]}", JsonElement.class);
    private static final JsonElement JSON_INNER_DATA = GSON.fromJson(
            "{\"config\":\"CONFIG_NAME\",\"name\":\"configRootName\",\"configCondition\":[\"locale\"],\"values\":"
            + "[{\"value\":\"valueHu\",\"locale\":[\"hu\"]},{\"value\":\"valueDefault\"}]}", JsonElement.class);
    @Mock
    private JsonDeserializationContext jsonDeserializationContext;

    private ConfigPackDeserializer configPackDeserializer;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        configPackDeserializer = new ConfigPackDeserializer();
    }

    @Test
    public void deserialize() {
        // GIVEN
        ConfigPack expected = createExpectedConfigPack();
        given(jsonDeserializationContext.deserialize(JSON_INNER_DATA, ConfigDefinition.class)).willReturn(CONFIG_DEFINITION);
        // WHEN
        ConfigPack actual = configPackDeserializer.deserialize(JSON_DATA, TYPE_TOKEN, jsonDeserializationContext);
        // THEN
        verify(jsonDeserializationContext).deserialize(JSON_INNER_DATA, ConfigDefinition.class);
        assertTrue(actual.getKeyToDefinitionsMap().entrySet().containsAll(expected.getKeyToDefinitionsMap().entrySet()));
        assertTrue(actual.getKeyToDefinitionsMap().keySet().containsAll(expected.getKeyToDefinitionsMap().keySet()));
        assertTrue(actual.getNameToDefinitions().keySet().containsAll(expected.getNameToDefinitions().keySet()));
        assertTrue(actual.getNameToDefinitions().keySet().containsAll(expected.getNameToDefinitions().keySet()));
    }

    private ConfigPack createExpectedConfigPack() {
        ConfigDefinition configDefinition = CONFIG_DEFINITION;
        ConfigKey configKey = new ConfigKey(configDefinition);
        return new ConfigPack(Map.of(configKey, configDefinition));
    }
}
