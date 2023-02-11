package com.unideb.qsa.config.resolver.deserializer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import com.unideb.qsa.config.resolver.domain.context.ConfigDefinition;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.context.ConfigValue;

/**
 * Provides API for deserialization to domain objects.
 */
public final class JsonDeserializerHelper {

    private JsonDeserializerHelper() {
    }

    /**
     * Deserialize a given string to {@link ConfigDefinition}.
     * @param fileContent The fileContent to deserialize.
     * @return instance of {@link ConfigDefinition}.
     */
    public static ConfigDefinition deserializeToConfigDefinition(String fileContent) {
        return register().create().fromJson(fileContent, ConfigDefinition.class);
    }

    /**
     * Deserialize a given string to a {@link ConfigPack}.
     * @param configPackJson The configPackJson to deserialize.
     * @return instance of {@link ConfigPack}.
     */
    public static ConfigPack deserializeToConfigPack(String configPackJson) {
        return new ConfigPack(register().create().fromJson(configPackJson, ConfigPack.class).getKeysToDefinitions());
    }

    /**
     * Deserialize a given json arrays into a {@link ConfigPack}.
     * @param configJsonArray as json array
     * @return instance of {@link ConfigPack}
     */
    public static ConfigPack deserializeToConfigPack(JsonArray configJsonArray) {
        return deserializeToConfigPack(configJsonArray.toString());
    }

    private static GsonBuilder register() {
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ConfigPack.class, new ConfigPackDeserializer());
        gsonBuilder.registerTypeAdapter(ConfigDefinition.class, new ConfigDefinitionDeserializer());
        gsonBuilder.registerTypeAdapter(ConfigValue.class, new ConfigValueDeserializer());
        return gsonBuilder;
    }

}
