package com.unideb.qsa.domain.deserializer.elements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

/**
 * Deserializer for config name.
 */
public final class ConfigNameElement implements ConfigDefinitionElement<String> {

    private String configElement;

    @Override
    public void populate(JsonElement jsonElement, JsonDeserializationContext context) {
        configElement = jsonElement.getAsString();
    }

    @Override
    public String getValue() {
        return configElement;
    }
}
