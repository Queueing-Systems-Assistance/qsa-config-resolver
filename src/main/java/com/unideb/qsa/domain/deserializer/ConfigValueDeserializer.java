package com.unideb.qsa.domain.deserializer;

import static com.unideb.qsa.domain.deserializer.DeserializationConstants.VALUE_ELEMENT;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static final String ERROR_MESSAGE_VALUE_NOT_FOUND = "'value' is missing value attribute: %s";
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public ConfigValue deserialize(JsonElement jsonElement, Type typeOfSrc, JsonDeserializationContext jsonDeserializationContext) {
        var value = getValue(jsonElement);
        var qualifiers = getQualifiers(jsonElement, jsonDeserializationContext);
        return new ConfigValue(value, qualifiers);
    }

    private Map<String, Set<String>> getQualifiers(JsonElement jsonElement, JsonDeserializationContext context) {
        return jsonElement.getAsJsonObject()
                          .entrySet()
                          .stream()
                          .filter(entry -> !entry.getKey().equals(VALUE_ELEMENT))
                          .collect(Collectors.toMap(Entry::getKey, entry -> new HashSet<>(context.deserialize(entry.getValue(), Set.class))));
    }

    private String getValue(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject()
                          .entrySet()
                          .stream()
                          .filter(entry -> entry.getKey().equals(VALUE_ELEMENT))
                          .findAny()
                          .map(Entry::getValue)
                          .map(valueJsonElement -> valueJsonElement.isJsonPrimitive() ? valueJsonElement.getAsString() : GSON.toJson(valueJsonElement))
                          .orElseThrow(() -> new ConfigValueException(String.format(ERROR_MESSAGE_VALUE_NOT_FOUND, jsonElement.toString())));
    }
}
