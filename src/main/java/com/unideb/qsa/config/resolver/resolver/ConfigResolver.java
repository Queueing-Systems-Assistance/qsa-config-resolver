package com.unideb.qsa.config.resolver.resolver;

import java.util.Optional;

import com.unideb.qsa.config.resolver.domain.context.Qualifier;

/**
 * Resolves configs based on the given qualifier. If there is no acceptable config, an empty {@link Optional} will return.
 */
public interface ConfigResolver {

    /**
     * Resolve config based on the qualifier.
     * @param configName name of the config
     * @param qualifier  conditions
     * @return resolved value
     */
    Optional<String> resolve(String configName, Qualifier qualifier);


    /**
     * Resolve config based on the qualifier into predefined class.
     * @param configName name of the config
     * @param qualifier  conditions
     * @param classOfT   the class of T
     * @return resolved value
     */
    <T> Optional<T> resolve(String configName, Qualifier qualifier, Class<T> classOfT);
}
