package com.unideb.qsa.config.resolver.datasource.configpack;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.unideb.qsa.config.resolver.domain.context.ConfigPack;
import com.unideb.qsa.config.resolver.domain.exception.ConfigPackException;

/**
 * Unit tests for {@link UrlConfigPackSource}.
 */
@Listeners(MockitoTestNGListener.class)
public class UrlConfigPackSourceTest {

    private static final ConfigPack CONFIG_PACK = new ConfigPack(Map.of());
    private static final String VALID_URL = "https://data/config2";
    private static final String INVALID_URL = "https://data/data";
    private static final URI VALID_URI = URI.create(VALID_URL);
    private static final HttpRequest HTTP_REQUEST = HttpRequest.newBuilder(VALID_URI).GET().build();
    private static final URI INVALID_URI = URI.create(INVALID_URL);
    private static final HttpRequest HTTP_INVALID_REQUEST = HttpRequest.newBuilder(INVALID_URI).GET().build();
    private static final HttpResponse.BodyHandler<String> BODY_HANDLER = HttpResponse.BodyHandlers.ofString();
    private static final String JSON_CONFIG = "{\"config\":[]}";

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> httpResponse;

    @Test
    public void getConfigPacks() throws IOException, InterruptedException {
        // GIVEN
        var expected = List.of(CONFIG_PACK);
        var urlConfigPackSource = new UrlConfigPackSource(List.of(VALID_URL), httpClient);
        given(httpClient.send(HTTP_REQUEST, BODY_HANDLER)).willReturn(httpResponse);
        given(httpResponse.body()).willReturn(JSON_CONFIG);
        // WHEN
        var actual = urlConfigPackSource.getConfigPacks();
        // THEN
        verify(httpClient).send(HTTP_REQUEST, BODY_HANDLER);
        verify(httpResponse).body();
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = ConfigPackException.class)
    public void getConfigPacksShouldThrowExceptionWhenFileErrorOccurs() throws IOException, InterruptedException {
        // GIVEN
        var urlConfigPackSource = new UrlConfigPackSource(List.of(INVALID_URL), httpClient);
        given(httpClient.send(HTTP_INVALID_REQUEST, BODY_HANDLER)).willThrow(InterruptedException.class);
        // WHEN
        urlConfigPackSource.getConfigPacks();
        // THEN
    }

}
