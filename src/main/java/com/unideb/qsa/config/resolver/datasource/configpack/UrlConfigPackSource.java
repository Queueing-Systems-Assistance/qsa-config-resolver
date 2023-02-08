package com.unideb.qsa.config.resolver.datasource.configpack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.deserializer.JsonDeserializerHelper;
import com.unideb.qsa.config.resolver.domain.exception.ConfigPackException;

/**
 * Resolves {@link ConfigPack} from URL.
 */
public class UrlConfigPackSource implements ConfigPackSource {

    private final HttpClient httpClient;
    private final List<String> configUris;

    public UrlConfigPackSource(List<String> configUris, HttpClient httpClient) {
        this.configUris = configUris;
        this.httpClient = httpClient;
    }

    @Override
    public List<ConfigPack> getConfigPacks() {
        return configUris.stream()
                         .filter(this::isConfigLocationURL)
                         .map(URI::create)
                         .map(this::getResponse)
                         .map(HttpResponse::body)
                         .map(JsonDeserializerHelper::deserializeToConfigPack)
                         .collect(Collectors.toList());
    }

    private HttpResponse<String> getResponse(URI configPackUri) {
        try {
            return httpClient.send(HttpRequest.newBuilder(configPackUri).GET().build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ConfigPackException(String.format("Cannot fetch config pack, uri [%s]", configPackUri));
        }
    }
}
