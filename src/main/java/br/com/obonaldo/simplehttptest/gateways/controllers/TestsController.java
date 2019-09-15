package br.com.obonaldo.simplehttptest.gateways.controllers;

import br.com.obonaldo.simplehttptest.gateways.HttpGateway;
import br.com.obonaldo.simplehttptest.gateways.controllers.resource.StepResult;
import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestResult;
import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestStepConfigResource;
import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestConfigResource;
import br.com.obonaldo.simplehttptest.gateways.parsers.ResponseParser;
import br.com.obonaldo.simplehttptest.gateways.parsers.VariablesParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestsController {

    private final HttpGateway httpGateway;
    private final VariablesParser variablesParser;
    private final Map<String, ResponseParser> responseParsers;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestResult runTest(@RequestBody TestConfigResource testConfigResource ) {

        final Map<String, String> variables = new HashMap<>();
        final TestResult testResult = new TestResult(testConfigResource.getName());

        for (TestStepConfigResource testStep : testConfigResource.getSteps()) {

            final StepResult stepResult = new StepResult(testStep.getName());
            testStep.resolvePathParams(variables);
            String response;

            try {
                if (testStep.hasRetry()) {
                    response = httpGateway.executeRequestWithValidation(testStep);

                } else {
                    response = httpGateway.executeRequest(testStep);
                }

                variables.putAll(variablesParser.mapResponseVariables(testStep, response));

                if (testStep.hasAsserts()) {
                    testStep.getResponse()
                            .getAsserts()
                            .forEach((node, expectedValue) -> {
                                responseParsers
                                        .get(testStep.getResponse().getType()).getValueFrom(response, node)
                                        .ifPresent(actualValue -> stepResult.addAssert(node, expectedValue, actualValue));
                            });
                }
            } catch (final Exception e) {
                stepResult.setError(e.getMessage());
            }
            testResult.addStep(stepResult);
        }
        return testResult;
    }
}