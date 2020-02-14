package com.unideb.qsa.domain.deserializer;

import static com.unideb.qsa.domain.deserializer.DeserializationConstants.VALUE_ELEMENT;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import com.unideb.qsa.domain.context.ConfigValue;
import com.unideb.qsa.domain.exception.ConfigValueException;

/**
 * Deserializes a single config value from a config definition json file into a {@link ConfigValue} object.
 */
public class ConfigValueDeserializer implements JsonDeserializer<ConfigValue> {

    private static final String VALUE_EXCEPTION = "'value' is missing value attribute: %s";
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public ConfigValue deserialize(JsonElement jsonElement, Type typeOfSrc, JsonDeserializationContext jsonDeserializationContext) {
        String value = getValue(jsonElement);
        ImmutableMap<String, ImmutableSet<String>> qualifiers = getQualifiers(jsonElement, jsonDeserializationContext);
        return new ConfigValue(value, qualifiers);
    }

    private ImmutableMap<String, ImmutableSet<String>> getQualifiers(JsonElement jsonElement, JsonDeserializationContext context) {
        ImmutableMap.Builder<String, ImmutableSet<String>> qualifiersMapBuilder = new ImmutableMap.Builder<>();
        jsonElement.getAsJsonObject()
                   .entrySet()
                   .stream()
                   .filter(entry -> !entry.getKey().equals(VALUE_ELEMENT))
                   .forEach(entry -> qualifiersMapBuilder.put(entry.getKey(),
                           new ImmutableSet.Builder<String>().addAll((Set<String>) context.deserialize(entry.getValue(), Set.class)).build()));
        return qualifiersMapBuilder.build();
    }

    private String getValue(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject()
                          .entrySet()
                          .stream()
                          .filter(entry -> entry.getKey().equals(VALUE_ELEMENT))
                          .findAny()
                          .map(Entry::getValue)
                          .map(valueJsonElement -> valueJsonElement.isJsonPrimitive() ? valueJsonElement.getAsString() : gson.toJson(valueJsonElement))
                          .orElseThrow(() -> new ConfigValueException(String.format(VALUE_EXCEPTION, jsonElement.toString())));
    }
}
