package br.com.obonaldo.simplehttptest.gateways.controllers;

import br.com.obonaldo.simplehttptest.gateways.HttpGateway;
import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestStepConfigResource;
import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestConfigResource;
import br.com.obonaldo.simplehttptest.gateways.parsers.ResponseParser;
import br.com.obonaldo.simplehttptest.gateways.parsers.VariablesParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestsController {

    private final HttpGateway httpGateway;
    private final VariablesParser parser;
    private final Map<String, ResponseParser> parsers;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity runTest(@RequestBody TestConfigResource testConfigResource ) {

        final Map<String, String> variables = new HashMap<>();
        final Map<String, String> results = new HashMap<>();

        for (TestStepConfigResource testStep : testConfigResource.getSteps()) {

            testStep.resolvePathParams(variables);
            String response;

            if (testStep.hasRetry()) {
                response = httpGateway.executeRequestWithValidation(testStep);

            } else {
                response = httpGateway.executeRequest(testStep);
            }

            variables.putAll(parser.mapResponseVariables(testStep, response));

            if (testStep.hasAsserts()) {
                testStep.getResponse()
                        .getAsserts()
                        .forEach((node, expectedValue) -> {
                            final String actualValue = parsers.get(testStep.getResponse().getType()).getValueFrom(response, node);
                            results.put(expectedValue, actualValue);
                });
            }
        }

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(results);
    }
}