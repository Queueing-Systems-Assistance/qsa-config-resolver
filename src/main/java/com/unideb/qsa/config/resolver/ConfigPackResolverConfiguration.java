package com.unideb.qsa.config.resolver;

import java.net.http.HttpClient;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unideb.qsa.config.resolver.datasource.configdefinition.ConfigDefinitionSource;
import com.unideb.qsa.config.resolver.datasource.configpack.CacheableConfigPackSource;
import com.unideb.qsa.config.resolver.datasource.configpack.CompositeConfigPackSource;
import com.unideb.qsa.config.resolver.datasource.configpack.ConfigPackSource;
import com.unideb.qsa.config.resolver.datasource.configpack.FileConfigPackSource;
import com.unideb.qsa.config.resolver.datasource.configpack.UrlConfigPackSource;
import com.unideb.qsa.config.resolver.resolver.ConfigResolver;
import com.unideb.qsa.config.resolver.resolver.DefaultConfigResolver;

/**
 * Configuration for resolving config packs.
 */
public class ConfigPackResolverConfiguration {

    private static final String REFRESH_RATE_AND_CONFIGS = "Config resolver refresh rate is [{}] minutes and configs are {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPackResolverConfiguration.class);
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private final ConfigDefinitionSource configDefinitionSource;

    public ConfigPackResolverConfiguration(List<String> configUris, int refreshRateInMinutes) {
        var source = createCompositeConfigPackSource(configUris);
        var compositeConfigPackSource = new CacheableConfigPackSource(source, refreshRateInMinutes);
        this.configDefinitionSource = new ConfigDefinitionSource(compositeConfigPackSource);
        LOGGER.info(REFRESH_RATE_AND_CONFIGS, refreshRateInMinutes, configUris);
    }

    /**
     * Creates a {@link ConfigResolver} instance. The instance supports cache and file or url config resolve.
     */
    public ConfigResolver createConfigResolver() {
        return new DefaultConfigResolver(configDefinitionSource);
    }

    private ConfigPackSource createCompositeConfigPackSource(List<String> configUris) {
        return new CompositeConfigPackSource(List.of(
                new FileConfigPackSource(configUris),
                new UrlConfigPackSource(configUris, HTTP_CLIENT))
        );
    }
}
