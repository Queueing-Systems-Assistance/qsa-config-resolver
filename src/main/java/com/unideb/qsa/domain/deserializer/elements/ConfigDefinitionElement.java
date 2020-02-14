package com.unideb.qsa.domain.deserializer.elements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

/**
 * Deserializer for the config elements.
 *
 * @param <T> config element type
 */
public interface ConfigDefinitionElement<T> {

    /**
     * Populates the given {@link T} element from the json.
     *
     * @param jsonElement config in json
     * @param context     holds different deserializers
     */
    void populate(JsonElement jsonElement, JsonDeserializationContext context);

    /**
     * Returns the populated config element.
     *
     * @return given {@link T} element
     */
    T getValue();
}
