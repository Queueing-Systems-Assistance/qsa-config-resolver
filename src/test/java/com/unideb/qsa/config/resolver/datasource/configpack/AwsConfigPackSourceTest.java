package com.unideb.qsa.config.resolver.datasource.configpack;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;


/**
 * Unit tests for {@link AwsConfigPackSource}.
 */
@Listeners(MockitoTestNGListener.class)
public class AwsConfigPackSourceTest {

    private static final ConfigPack CONFIG_PACK = new ConfigPack(Map.of());
    private static final String CONFIG_PACK_CONTENT = "{\"config\":[]}";
    private static final String LAMBDA_FUNCTION_NAME = "lambda-function";
    private static final InvokeResponse INVOKE_RESULT = InvokeResponse.builder().payload(SdkBytes.fromUtf8String(CONFIG_PACK_CONTENT)).build();
    private static final InvokeRequest INVOKE_REQUEST = InvokeRequest.builder().functionName(LAMBDA_FUNCTION_NAME).build();

    @Mock
    private LambdaClient awsLambdaClient;

    private AwsConfigPackSource awsConfigPackSource;

    @BeforeMethod
    public void setup() {
        awsConfigPackSource = new AwsConfigPackSource(List.of(LAMBDA_FUNCTION_NAME), awsLambdaClient);
    }

    @Test
    public void getConfigPacks() {
        // GIVEN
        var expected = List.of(CONFIG_PACK);
        given(awsLambdaClient.invoke(INVOKE_REQUEST)).willReturn(INVOKE_RESULT);
        // WHEN
        var actual = awsConfigPackSource.getConfigPacks();
        // THEN
        assertEquals(actual, expected);
    }

}
