package com.unideb.qsa.config.resolver.deserializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigKey;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * Deserializes a config pack from a single json file.
 */
public class ConfigPackDeserializer implements JsonDeserializer<ConfigPack> {

    private static final String CONFIG = "config";

    @Override
    public ConfigPack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var jsonObject = json.getAsJsonObject();
        var configDefinitionsJsonArray = jsonObject.get(CONFIG).getAsJsonArray();
        var keysToDefinitions = create(context, configDefinitionsJsonArray);
        return new ConfigPack(keysToDefinitions);
    }

    private Map<ConfigKey, ConfigDefinition> create(JsonDeserializationContext context, JsonArray configDefinitions) {
        return StreamSupport.stream(configDefinitions.spliterator(), true)
                            .map(configDefinitionJsonElement -> (ConfigDefinition) context.deserialize(configDefinitionJsonElement, ConfigDefinition.class))
                            .collect(Collectors.toMap(configDefinition -> new ConfigKey(configDefinition.name(), configDefinition.qualifiers()),
                                    propertyDefinition -> propertyDefinition));
    }
}
