package br.com.obonaldo.simplehttptest.gateways;

import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestStepConfigResource;
import br.com.obonaldo.simplehttptest.gateways.parsers.ResponseParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpGateway {

    private final RestTemplate restTemplate;
    private final Map<String, ResponseParser> parsers;

    public String executeRequest(final TestStepConfigResource requestConfig) {
        return request(requestConfig);
    }

    @Retryable(
            value = IllegalArgumentException.class,
            maxAttempts = 30,
            backoff = @Backoff(delay = 1000))
    public String executeRequestWithValidation(final TestStepConfigResource requestConfig) {

        final String response = request(requestConfig);
        final String responseType = requestConfig.getResponse().getType();
        final Map<String, String> retries = requestConfig.getResponse().getRetries();

        retries.keySet()
                .forEach(key -> {
                    final String expectedValue = retries.get(key);
                    final String actualValue = parsers.get(responseType).getValueFrom(response, key)
                            .orElseThrow(() -> new IllegalArgumentException(format("Expected value not found for node: [%s] [Expected]: [%s]", key, expectedValue)));
                    if (!expectedValue.equals(actualValue))
                        throw new IllegalArgumentException(format("Unexpected value found for node: [%s] [Expected]: [%s] [Actual]: [%s]", key, expectedValue, actualValue));
                });

        return response;
    }

    @Retryable(
            value = HttpClientErrorException.class,
            backoff = @Backoff(delay = 1000))
    private String request(final TestStepConfigResource requestConfig) {

        final String url = requestConfig.getUrl();
        final MultiValueMap headers = buildMultiValueMap(requestConfig.getHeaders());
        final String payload = requestConfig.getPayload();
        final Map<String, String> queryParams = requestConfig.getQueryParams();
        final String[] pathParams = requestConfig.getPathParams().toArray(new String[0]);
        final HttpMethod method = HttpMethod.resolve(requestConfig.getMethod());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);

        if (!isEmpty(queryParams))
            uriBuilder.queryParams(buildMultiValueMap(queryParams));

        final URI uri = uriBuilder.buildAndExpand(pathParams).toUri();

        final HttpEntity httpEntity = new HttpEntity<>(payload, headers);

        log.info("Executing request: {}", uri.toString());

        final ResponseEntity<String> response = restTemplate
                .exchange(
                        uri,
                        method,
                        httpEntity,
                        String.class);

        log.info("Received response: {}", response.getBody());

        return response.getBody();
    }

    private MultiValueMap buildMultiValueMap(final Map<String, String> headers) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.forEach(map::add);
        return map;
    }
}