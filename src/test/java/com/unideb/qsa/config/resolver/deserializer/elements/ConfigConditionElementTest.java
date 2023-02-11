package com.unideb.qsa.config.resolver.deserializer.elements;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

/**
 * Unit tests for {@link ConfigConditionElement}.
 */
@Listeners(MockitoTestNGListener.class)
public class ConfigConditionElementTest {

    private static final List<String> FILLED_CONFIG_CONDITION = List.of("locale", "feature");
    private static final List<String> EMPTY_CONFIG_CONDITION = List.of();
    private static final Gson GSON = new Gson();
    private static final JsonElement FILLED_JSON_DATA = GSON.fromJson("[\"locale\", \"feature\"]", JsonElement.class);
    private static final JsonElement EMPTY_JSON_DATA = GSON.fromJson("[]", JsonElement.class);
    @Mock
    private JsonDeserializationContext jsonDeserializationContext;
    private ConfigConditionElement configConditionElement = new ConfigConditionElement();

    @BeforeMethod
    public void setup() {
        configConditionElement = new ConfigConditionElement();
    }

    @DataProvider
    public Object[][] data() {
        return new Object[][]{
                {FILLED_JSON_DATA, FILLED_CONFIG_CONDITION},
                {EMPTY_JSON_DATA, EMPTY_CONFIG_CONDITION}
        };
    }

    @Test(dataProvider = "data")
    public void populateWithElements(JsonElement jsonData, List<String> expected) {
        // GIVEN
        // WHEN
        configConditionElement.populate(jsonData, jsonDeserializationContext);
        List<String> actual = configConditionElement.getValue();
        // THEN
        verifyNoInteractions(jsonDeserializationContext);
        assertEquals(actual, expected);
    }
}
