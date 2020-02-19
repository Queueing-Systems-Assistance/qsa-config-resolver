package com.unideb.qsa.config.resolver.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration class for this library.
 */
@ConfigurationProperties("qsa.config")
public class ResolverProperties {

    private List<String> uris;

    public List<String> getUris() {
        return uris;
    }

    public void setUris(final List<String> uris) {
        this.uris = uris;
    }
}
