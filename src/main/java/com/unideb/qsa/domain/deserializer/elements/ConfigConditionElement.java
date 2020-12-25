package com.unideb.qsa.domain.deserializer.elements;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

/**
 * Deserializer for config condition elements.
 */
public final class ConfigConditionElement implements ConfigDefinitionElement<List<String>> {

    private List<String> configCondition;

    @Override
    public void populate(JsonElement jsonElement, JsonDeserializationContext context) {
        configCondition = StreamSupport.stream(jsonElement.getAsJsonArray().spliterator(), true)
                                       .map(JsonElement::getAsString)
                                       .collect(Collectors.toList());
    }

    @Override
    public List<String> getValue() {
        return Optional.ofNullable(configCondition).orElse(List.of());
    }
}
