package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * {@link ConfigPack} resolver.
 */
public interface ConfigPackSource {

    String HTTP = "http://";
    String HTTPS = "https://";

    /**
     * Resolves {@link ConfigPack}s.
     * @return resolved {@link ConfigPack} in a list
     */
    List<ConfigPack> getConfigPacks();

    /**
     * Check if the given location is an URL or not.
     * @param configLocation location
     * @return true, if the location is an URL, false otherwise
     */
    default boolean isConfigLocationURL(String configLocation) {
        return configLocation.startsWith(HTTP) || configLocation.startsWith(HTTPS);
    }
}
