package com.unideb.qsa.domain.deserializer;

import com.google.gson.GsonBuilder;

import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.context.ConfigValue;

/**
 * Provides API for deserialization to domain objects.
 */
public final class JsonDeserializerHelper {

    private JsonDeserializerHelper() {
    }

    private static GsonBuilder register() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ConfigDefinition.class, new ConfigDefinitionDeserializer());
        gsonBuilder.registerTypeAdapter(ConfigValue.class, new ConfigValueDeserializer());
        return gsonBuilder;
    }

    /**
     * Deserialize fileContent to {@link ConfigDefinition}.
     *
     * @param fileContent The fileContent to deserialize.
     * @return instance of {@link ConfigDefinition}.
     */
    public static ConfigDefinition deserializeToConfigDefinition(String fileContent) {
        return register().create().fromJson(fileContent, ConfigDefinition.class);
    }
}
