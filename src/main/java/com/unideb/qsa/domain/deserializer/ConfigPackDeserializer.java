package com.unideb.qsa.domain.deserializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        JsonArray configDefinitionsJsonArray = jsonObject.get(CONFIG).getAsJsonArray();
        Map<ConfigKey, ConfigDefinition> configKeyToConfigDefinitionMap = create(context, configDefinitionsJsonArray);
        return new ConfigPack(configKeyToConfigDefinitionMap);
    }

    private Map<ConfigKey, ConfigDefinition> create(JsonDeserializationContext context, JsonArray configDefinitionsJsonArray) {
        return StreamSupport.stream(configDefinitionsJsonArray.spliterator(), true)
                            .map(configDefinitionJsonElement -> (ConfigDefinition) context.deserialize(configDefinitionJsonElement, ConfigDefinition.class))
                            .collect(Collectors.toMap(ConfigKey::new, propertyDefinition -> propertyDefinition));
    }
}
