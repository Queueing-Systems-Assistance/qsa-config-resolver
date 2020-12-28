package com.unideb.qsa.domain.deserializer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

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

import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.context.ConfigValue;

/**
 * Unit tests for {@link ConfigDefinitionDeserializer}.
 */
public class ConfigDefinitionDeserializerTest {

    public static final String CONFIG_NAME = "CONFIG_NAME";
    public static final Map<String, String> CONFIG_QUALIFIER = Map.of("name", "configRootQualifier");
    private static final Type TYPE_TOKEN = new TypeToken<ConfigValue>() {}.getType();
    private static final Gson GSON = new Gson();
    private static final JsonElement JSON_DATA = GSON.fromJson(
            "{\"config\":\"CONFIG_NAME\",\"name\":\"configRootQualifier\",\"configCondition\":[\"locale\"],"
            + "\"values\":[{\"value\":\"qualifierValue\",\"locale\":[\"hu\"]},{\"value\":\"defaultValue\"}]}", JsonElement.class);
    private static final JsonElement JSON_VALUE_DEFAULT = GSON.fromJson("{\"value\":\"defaultValue\"}", JsonElement.class);
    private static final JsonElement JSON_VALUE_QUALIFIER = GSON.fromJson("{\"value\":\"qualifierValue\",\"locale\":[\"hu\"]}", JsonElement.class);
    private static final ConfigValue CONFIG_VALUE_QUALIFIER = new ConfigValue("qualifierValue", Map.of("locale", Set.of("hu")));
    private static final ConfigValue CONFIG_VALUE_DEFAULT = new ConfigValue("defaultValue", Map.of());
    @Mock
    private JsonDeserializationContext jsonDeserializationContext;

    private ConfigDefinitionDeserializer configDefinitionDeserializer;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        configDefinitionDeserializer = new ConfigDefinitionDeserializer();
    }

    @Test
    public void deserialize() {
        // GIVEN
        ConfigDefinition expected = new ConfigDefinition(CONFIG_NAME, Set.of(CONFIG_VALUE_DEFAULT, CONFIG_VALUE_QUALIFIER), CONFIG_QUALIFIER);
        given(jsonDeserializationContext.deserialize(JSON_VALUE_DEFAULT, TYPE_TOKEN)).willReturn(CONFIG_VALUE_DEFAULT);
        given(jsonDeserializationContext.deserialize(JSON_VALUE_QUALIFIER, TYPE_TOKEN)).willReturn(CONFIG_VALUE_QUALIFIER);
        // WHEN
        ConfigDefinition actual = configDefinitionDeserializer.deserialize(JSON_DATA, TYPE_TOKEN, jsonDeserializationContext);
        // THEN
        verify(jsonDeserializationContext).deserialize(JSON_VALUE_DEFAULT, TYPE_TOKEN);
        verify(jsonDeserializationContext).deserialize(JSON_VALUE_QUALIFIER, TYPE_TOKEN);
        assertEquals(actual.getConfigValues(), expected.getConfigValues());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getQualifiers(), expected.getQualifiers());
    }
}
