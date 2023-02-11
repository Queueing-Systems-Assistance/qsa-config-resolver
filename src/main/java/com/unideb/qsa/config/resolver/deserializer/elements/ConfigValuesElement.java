package com.unideb.qsa.config.resolver.deserializer.elements;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import com.unideb.qsa.config.resolver.domain.context.ConfigValue;

/**
 * Deserializer for {@link ConfigValue}.
 */
public final class ConfigValuesElement implements ConfigDefinitionElement<List<ConfigValue>> {

    private List<ConfigValue> configValues;

    @Override
    public void populate(JsonElement jsonElement, JsonDeserializationContext context) {
        configValues = new ArrayList<>();
        for (var element : jsonElement.getAsJsonArray()) {
            var configValue = (ConfigValue) context.deserialize(element.getAsJsonObject(), ConfigValue.class);
            configValues.add(configValue);
        }
    }

    @Override
    public List<ConfigValue> getValue() {
        return configValues;
    }
}
