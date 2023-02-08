package com.unideb.qsa.config.resolver.domain.deserializer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
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

import com.unideb.qsa.config.resolver.domain.context.ConfigValue;
import com.unideb.qsa.config.resolver.domain.exception.ConfigValueException;

/**
 * Unit tests for {@link ConfigValueDeserializer}.
 */
public class ConfigValueDeserializerTest {

    private static final Type TYPE_TOKEN = new TypeToken<ConfigValue>() {}.getType();
    private static final Gson GSON = new Gson();
    private static final String VALUE_TEST_VALUE = "testValue";
    private static final Set<String> QUALIFIER_VALUES = Set.of("hu", "ru");
    private static final String QUALIFIER_NAME_LOCALE = "locale";
    private static final JsonElement JSON_DATA = GSON.fromJson("{\"value\":\"testValue\",\"locale\":[\"hu\",\"ru\"]}", JsonElement.class);
    private static final JsonElement EMPTY_VALUE_JSON_DATA = GSON.fromJson("{\"locale\":[\"hu\",\"ru\"]}", JsonElement.class);
    private static final JsonElement EMPTY_QUALIFIER_JSON_DATA = GSON.fromJson("{\"value\":\"testValue\"}", JsonElement.class);
    private static final JsonElement JSON_QUALIFIER_VALUES = GSON.fromJson("[\"hu\",\"ru\"]", JsonElement.class);

    @Mock
    private JsonDeserializationContext jsonDeserializationContext;

    private ConfigValueDeserializer configValueDeserializer;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        configValueDeserializer = new ConfigValueDeserializer();
    }

    @Test
    public void deserializeShouldWorkWithQualifier() {
        // GIVEN
        ConfigValue expected = new ConfigValue(VALUE_TEST_VALUE, Map.of(QUALIFIER_NAME_LOCALE, QUALIFIER_VALUES));
        given(jsonDeserializationContext.deserialize(JSON_QUALIFIER_VALUES, Set.class)).willReturn(QUALIFIER_VALUES);
        // WHEN
        ConfigValue actual = configValueDeserializer.deserialize(JSON_DATA, TYPE_TOKEN, jsonDeserializationContext);
        // THEN
        verify(jsonDeserializationContext).deserialize(JSON_QUALIFIER_VALUES, Set.class);
        assertEquals(actual.getValue(), expected.getValue());
        assertTrue(actual.getQualifiers().keySet().containsAll(expected.getQualifiers().keySet()));
        assertTrue(actual.getQualifiers().entrySet().containsAll(expected.getQualifiers().entrySet()));
    }


    @Test
    public void deserializeShouldWorkWithoutQualifier() {
        // GIVEN
        ConfigValue expected = new ConfigValue(VALUE_TEST_VALUE, Map.of());
        // WHEN
        ConfigValue actual = configValueDeserializer.deserialize(EMPTY_QUALIFIER_JSON_DATA, TYPE_TOKEN, jsonDeserializationContext);
        // THEN
        verifyNoInteractions(jsonDeserializationContext);
        assertEquals(actual.getValue(), expected.getValue());
        assertTrue(actual.getQualifiers().keySet().containsAll(expected.getQualifiers().keySet()));
        assertTrue(actual.getQualifiers().entrySet().containsAll(expected.getQualifiers().entrySet()));
    }

    @Test(expectedExceptions = ConfigValueException.class)
    public void deserializeShouldThrowExceptionIfValueIsNotPresent() {
        // GIVEN
        // WHEN
        configValueDeserializer.deserialize(EMPTY_VALUE_JSON_DATA, TYPE_TOKEN, jsonDeserializationContext);
        // THEN
    }
}
