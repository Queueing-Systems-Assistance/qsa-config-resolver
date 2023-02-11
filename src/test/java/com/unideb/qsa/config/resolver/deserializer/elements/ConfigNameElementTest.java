package com.unideb.qsa.config.resolver.deserializer.elements;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.testng.Assert.assertEquals;

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
 * Unit tests for {@link ConfigNameElement}.
 */
@Listeners(MockitoTestNGListener.class)
public class ConfigNameElementTest {

    private static final String FILLED_CONFIG_NAME = "configName";
    private static final String EMPTY_CONFIG_NAME = "";
    private static final Gson GSON = new Gson();
    private static final JsonElement FILLED_JSON_DATA = GSON.fromJson("configName", JsonElement.class);
    private static final JsonElement EMPTY_JSON_DATA = GSON.fromJson("\"\"", JsonElement.class);

    @Mock
    private JsonDeserializationContext jsonDeserializationContext;

    private ConfigNameElement configNameElement;

    @BeforeMethod
    public void setup() {
        configNameElement = new ConfigNameElement();
    }

    @DataProvider
    public Object[][] data() {
        return new Object[][]{
                {FILLED_JSON_DATA, FILLED_CONFIG_NAME},
                {EMPTY_JSON_DATA, EMPTY_CONFIG_NAME}
        };
    }

    @Test(dataProvider = "data")
    public void populateWithElements(JsonElement jsonData, String expected) {
        // GIVEN
        // WHEN
        configNameElement.populate(jsonData, jsonDeserializationContext);
        String actual = configNameElement.getValue();
        // THEN
        verifyNoInteractions(jsonDeserializationContext);
        assertEquals(actual, expected);
    }
}
