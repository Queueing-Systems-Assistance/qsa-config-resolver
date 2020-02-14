package com.unideb.qsa.domain.deserializer.elements;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

/**
 * Deserializer for config condition elements.
 */
public final class ConfigConditionElement implements ConfigDefinitionElement<ImmutableList<String>> {

    private ImmutableList<String> configCondition;

    @Override
    public void populate(JsonElement jsonElement, JsonDeserializationContext context) {
        ImmutableList.Builder<String> configConditionBuilder = new ImmutableList.Builder<>();
        jsonElement.getAsJsonArray().forEach(element -> configConditionBuilder.add(element.getAsString()));
        configCondition = configConditionBuilder.build();
    }

    @Override
    public ImmutableList<String> getValue() {
        return Optional.ofNullable(configCondition).orElse(ImmutableList.of());
    }
}
