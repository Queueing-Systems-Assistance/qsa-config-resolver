package com.unideb.qsa.config.resolver.deserializer.elements;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import com.unideb.qsa.config.resolver.domain.context.ConfigValue;

/**
 * Unit tests for {@link ConfigValuesElement}.
 */
@Listeners(MockitoTestNGListener.class)
public class ConfigValuesElementTest {

    private static final Gson GSON = new Gson();
    private static final JsonElement JSON_VALUE_1 = GSON.fromJson("{\"value\":\"value1\",\"locale\":[\"hu\"]}", JsonElement.class);
    private static final JsonElement JSON_VALUE_2 = GSON.fromJson("{\"value\":\"value2\"}", JsonElement.class);
    private static final JsonElement JSON_DATA = GSON.fromJson("[{\"value\":\"value1\",\"locale\":[\"hu\"]},{\"value\":\"value2\"}]", JsonElement.class);
    private static final ConfigValue CONFIG_VALUE_1 = new ConfigValue("value1", Map.of("locale", Set.of("hu")));
    private static final ConfigValue CONFIG_VALUE_2 = new ConfigValue("value", Map.of());

    @Mock
    private JsonDeserializationContext jsonDeserializationContext;

    private ConfigValuesElement configValuesElement;

    @BeforeMethod
    public void setup() {
        configValuesElement = new ConfigValuesElement();
    }

    @Test
    public void populateWithElements() {
        // GIVEN
        List<ConfigValue> expected = List.of(CONFIG_VALUE_1, CONFIG_VALUE_2);
        given(jsonDeserializationContext.deserialize(JSON_VALUE_1, ConfigValue.class)).willReturn(CONFIG_VALUE_1);
        given(jsonDeserializationContext.deserialize(JSON_VALUE_2, ConfigValue.class)).willReturn(CONFIG_VALUE_2);
        // WHEN
        configValuesElement.populate(JSON_DATA, jsonDeserializationContext);
        List<ConfigValue> actual = configValuesElement.getValue();
        // THEN
        verify(jsonDeserializationContext).deserialize(JSON_VALUE_1, ConfigValue.class);
        verify(jsonDeserializationContext).deserialize(JSON_VALUE_2, ConfigValue.class);
        assertEquals(actual, expected);
    }
}
