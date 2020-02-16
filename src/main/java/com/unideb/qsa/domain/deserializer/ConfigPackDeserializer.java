package com.unideb.qsa.domain.deserializer;


import java.lang.reflect.Type;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.context.ConfigKey;
import com.unideb.qsa.domain.context.ConfigPack;

/**
 * Deserializes a config pack from a single json file.
 */
public class ConfigPackDeserializer implements JsonDeserializer<ConfigPack> {

    private static final String CONFIG = "config";

    @Override
    public ConfigPack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new ConfigPack.Builder(populate(jsonObject, context)).build();
    }

    private ImmutableMap<ConfigKey, ConfigDefinition> populate(JsonObject jsonObject, JsonDeserializationContext context) {
        JsonArray configDefinitionsJsonArray = jsonObject.get(CONFIG).getAsJsonArray();
        ImmutableMap.Builder<ConfigKey, ConfigDefinition> builder = new ImmutableMap.Builder<>();
        configDefinitionsJsonArray.forEach(configDefinitionJsonElement -> {
            ConfigDefinition propertyDefinition = context.deserialize(configDefinitionJsonElement, ConfigDefinition.class);
            builder.put(new ConfigKey(propertyDefinition), propertyDefinition);
        });
        return builder.build();
    }
}
