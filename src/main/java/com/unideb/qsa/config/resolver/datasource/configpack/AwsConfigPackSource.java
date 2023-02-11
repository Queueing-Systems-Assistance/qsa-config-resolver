package com.unideb.qsa.config.resolver.datasource.configpack;

import java.util.List;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import com.unideb.qsa.config.resolver.deserializer.JsonDeserializerHelper;
import com.unideb.qsa.config.resolver.domain.context.ConfigPack;

/**
 * Resolves {@link ConfigPack} from AWS Lambda content.
 */
public class AwsConfigPackSource implements ConfigPackSource {

    private final List<String> uris;
    private final LambdaClient client;

    public AwsConfigPackSource(List<String> uris, LambdaClient client) {
        this.uris = uris;
        this.client = client;
    }

    @Override
    public List<ConfigPack> getConfigPacks() {

        return uris.stream()
                   .map(uri -> InvokeRequest.builder().functionName(uri).build())
                   .map(client::invoke)
                   .map(invokeResult -> invokeResult.payload().asUtf8String())
                   .map(JsonDeserializerHelper::deserializeToConfigPack)
                   .collect(Collectors.toList());
    }

}
