package com.unideb.qsa.config.resolver;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.lambda.LambdaClient;

import com.unideb.qsa.config.resolver.datasource.configdefinition.ConfigDefinitionSource;
import com.unideb.qsa.config.resolver.datasource.configpack.AwsConfigPackSource;
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
public class ConfigPackResolverBuilder {

    private static final String ADDING_CONFIG_PACK_LOG = "Adding [{}] config pack source, {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPackResolverBuilder.class);
    private static final int REFRESH_DAILY_IN_MINUTES = 1440;

    private final List<ConfigPackSource> configPackSources = new ArrayList<>();
    private int refreshRateInMinutes = REFRESH_DAILY_IN_MINUTES;

    /**
     * Creates a {@link ConfigResolver} instance. It supports caching and will retrieve configs from the given sources.
     */
    public ConfigResolver build() {
        var compositeConfigPackSource = new CompositeConfigPackSource(configPackSources);
        var cacheableConfigPackSource = new CacheableConfigPackSource(compositeConfigPackSource, refreshRateInMinutes);
        var configDefinitionSource = new ConfigDefinitionSource(cacheableConfigPackSource);
        return new DefaultConfigResolver(configDefinitionSource);
    }

    /**
     * Retrieves configs from local files.
     * @param paths File path
     * @return {@link ConfigPackResolverBuilder}
     */
    public ConfigPackResolverBuilder withLocalPaths(List<String> paths) {
        configPackSources.add(new FileConfigPackSource(paths));
        LOGGER.info(ADDING_CONFIG_PACK_LOG, "FILE", paths);
        return this;
    }

    /**
     * Retrieves configs from via {@link HttpClient}.
     * @param client HTTP client
     * @return {@link ConfigPackResolverBuilder}
     */
    public ConfigPackResolverBuilder withUrls(HttpClient client, List<String> urls) {
        configPackSources.add(new UrlConfigPackSource(urls, client));
        LOGGER.info(ADDING_CONFIG_PACK_LOG, "URL", urls);
        return this;
    }

    /**
     * Retrieves configs from AWS Lambdas.
     * @param client AWS Lambda client
     * @param lambdas a list of lambda functions names
     * @return {@link ConfigPackResolverBuilder}
     */
    public ConfigPackResolverBuilder withAwsLambda(LambdaClient client, List<String> lambdas) {
        configPackSources.add(new AwsConfigPackSource(lambdas, client));
        LOGGER.info(ADDING_CONFIG_PACK_LOG, "AWS-LAMBDA", lambdas);
        return this;
    }

    /**
     * Refresh rate for the config packs.
     * @param refreshRateInMinutes time interval in minutes
     * @return {@link ConfigPackResolverBuilder}
     */
    public ConfigPackResolverBuilder withRefreshInMinutes(int refreshRateInMinutes) {
        this.refreshRateInMinutes = refreshRateInMinutes;
        LOGGER.info("Config resolver refresh rate is [{}] minutes", this.refreshRateInMinutes);
        return this;
    }
}
