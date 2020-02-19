package com.unideb.qsa.config.resolver.resolver;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.config.resolver.datasource.configdefinition.ConfigDefinitionSource;
import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.context.ConfigValue;
import com.unideb.qsa.domain.context.Qualifier;

/**
 * The default implementation for {@link ConfigResolver}r.
 */
@Component
public class DefaultConfigResolver implements ConfigResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigResolver.class);

    private static final String RESOLVE_ATTEMPT_LOG_MSG = "Attempting to resolve config={} for context={} and configDefinitions={}";
    private static final String RESOLVE_SUCCESS_LOG_MSG = "Resolved config={} to configValue=\"{}\" for context={}";
    private static final String RESOLVE_FAIL_LOG_MSG = "Failed to resolve config={} for context={} against the following configDefinitions={}";

    @Autowired
    private ConfigDefinitionSource configDefinitionSource;

    @Override
    public Optional<String> resolve(String configName, Qualifier qualifier) {
        Collection<ConfigDefinition> configDefinitions = configDefinitionSource.getConfigDefinitions(configName, qualifier);
        Optional<ConfigValue> resolvedConfigValue = resolve(configName, qualifier, configDefinitions);
        return resolvedConfigValue.map(ConfigValue::getValue);
    }

    private Optional<ConfigValue> getConfigDefinition(ConfigDefinition configDefinition, Qualifier context) {
        return configDefinition
                .getConfigValues()
                .stream()
                .filter(configValue -> doesConfigValueMatchContext(configValue, context))
                .findFirst();
    }

    private Optional<ConfigValue> resolve(String configName, Qualifier qualifier, Collection<ConfigDefinition> configDefinitions) {
        LOGGER.info(RESOLVE_ATTEMPT_LOG_MSG, configName, qualifier, configDefinitions);
        Optional<ConfigValue> resolvedConfigValue = resolveConfigValue(qualifier, configDefinitions);
        //CHECKSTYLE:OFF
        resolvedConfigValue.ifPresentOrElse(
                configValue -> LOGGER.info(RESOLVE_SUCCESS_LOG_MSG, configName, configValue.getValue(), qualifier),
                () -> LOGGER.info(RESOLVE_FAIL_LOG_MSG, configName, qualifier, configDefinitions));
        //CHECKSTYLE:ON
        return resolvedConfigValue;
    }

    private Optional<ConfigValue> resolveConfigValue(Qualifier qualifier, Collection<ConfigDefinition> configDefinitions) {
        return configDefinitions
                .stream()
                .map(configDefinition -> getConfigDefinition(configDefinition, qualifier))
                .filter(Optional::isPresent)
                .findAny()
                .orElse(Optional.empty());
    }

    private boolean doesConfigValueMatchContext(ConfigValue configValue, Qualifier givenQualifier) {
        return configValue
                .getQualifiers()
                .entrySet()
                .stream()
                .noneMatch(qualifier ->
                        !givenQualifier.containsKey(qualifier.getKey())
                        || !qualifier.getValue().contains(givenQualifier.get(qualifier.getKey())));
    }
}
