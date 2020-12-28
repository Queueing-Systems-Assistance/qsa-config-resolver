package com.unideb.qsa.domain.deserializer;

import static com.unideb.qsa.domain.deserializer.DeserializationConstants.CONFIG_CONDITION;
import static com.unideb.qsa.domain.deserializer.DeserializationConstants.CONFIG_ELEMENT;
import static com.unideb.qsa.domain.deserializer.DeserializationConstants.VALUES_ELEMENT;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import com.unideb.qsa.domain.context.ConfigConditionComparator;
import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.context.ConfigValue;
import com.unideb.qsa.domain.deserializer.elements.ConfigConditionElement;
import com.unideb.qsa.domain.deserializer.elements.ConfigDefinitionElement;
import com.unideb.qsa.domain.deserializer.elements.ConfigNameElement;
import com.unideb.qsa.domain.deserializer.elements.ConfigValuesElement;
import com.unideb.qsa.domain.exception.ConfigDefinitionException;

/**
 * Deserializer for a config definition json file into a {@link ConfigDefinition} object.
 */
public class ConfigDefinitionDeserializer implements JsonDeserializer<ConfigDefinition> {

    private static final String CONFIG_CONDITION_EXCEPTION = "ConfigDefinitionDeserializer 'configCondition' must not be null";
    private static final String CONFIG_VALUES_EXCEPTION = "ConfigDefinitionDeserializer 'values' must not be null or empty";
    private static final String CONFIG_CONDITION_QUALIFIER_EXCEPTION = "ConfigDefinitionDeserializer 'configCondition' must contain all qualifiers in use in the configDefinition";

    @Override
    public ConfigDefinition deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
        Map<String, ConfigDefinitionElement<?>> elements = createConfigDefinition();
        populateElements(jsonElement, context, elements);

        String configName = (String) elements.get(CONFIG_ELEMENT).getValue();
        Map<String, String> qualifiers = getQualifiers(jsonElement, elements);
        Set<ConfigValue> configValues = getConfigValues(elements);
        return new ConfigDefinition(configName, configValues, qualifiers);
    }

    private Map<String, ConfigDefinitionElement<?>> createConfigDefinition() {
        Map<String, ConfigDefinitionElement<?>> elements = new HashMap<>();
        elements.put(CONFIG_ELEMENT, new ConfigNameElement());
        elements.put(CONFIG_CONDITION, new ConfigConditionElement());
        elements.put(VALUES_ELEMENT, new ConfigValuesElement());
        return elements;
    }

    private void populateElements(JsonElement jsonElement, JsonDeserializationContext context, Map<String, ConfigDefinitionElement<?>> elements) {
        jsonElement.getAsJsonObject()
                   .entrySet()
                   .stream()
                   .filter(entry -> elements.containsKey(entry.getKey()))
                   .forEach(entry -> elements.get(entry.getKey()).populate(entry.getValue(), context));
    }

    private Map<String, String> getQualifiers(JsonElement jsonElement, Map<String, ConfigDefinitionElement<?>> elements) {
        return jsonElement.getAsJsonObject().entrySet()
                          .stream()
                          .filter(entry -> !elements.containsKey(entry.getKey()))
                          .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getAsString()));
    }

    private Set<ConfigValue> getConfigValues(Map<String, ConfigDefinitionElement<?>> elements) {
        List<ConfigValue> configValues = (List<ConfigValue>) elements.get(VALUES_ELEMENT).getValue();
        List<String> configCondition = (List<String>) elements.get(CONFIG_CONDITION).getValue();
        validateConditionAndQualifier(configValues, configCondition);
        configValues.sort(new ConfigConditionComparator(configCondition));
        return new LinkedHashSet<>(configValues);
    }

    private void validateConditionAndQualifier(Collection<ConfigValue> configValues, List<String> configCondition) {
        if (configValues == null || configValues.isEmpty()) {
            throw new ConfigDefinitionException(CONFIG_VALUES_EXCEPTION);
        }
        Set<String> usedQualifiers = new HashSet<>();
        configValues.forEach(configValue -> usedQualifiers.addAll(configValue.getQualifiers().keySet()));
        checkConfigQualifiers(configCondition, usedQualifiers);
    }

    private void checkConfigQualifiers(List<String> configCondition, Set<String> usedQualifiers) {
        if (!usedQualifiers.isEmpty()) {
            if (configCondition == null) {
                throw new ConfigDefinitionException(CONFIG_CONDITION_EXCEPTION);
            }
            if (!configCondition.containsAll(usedQualifiers)) {
                throw new ConfigDefinitionException(CONFIG_CONDITION_QUALIFIER_EXCEPTION);
            }
        }
    }
}
