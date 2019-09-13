package br.com.obonaldo.simplehttptest.gateways.parsers;

import br.com.obonaldo.simplehttptest.gateways.controllers.resource.TestStepConfigResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VariablesParser {

    private final Map<String, ResponseParser> parsers;

    public Map<String, String> mapResponseVariables(final TestStepConfigResource requestConfig, final String response) {

        final Map<String, String> variables = requestConfig.getResponse().getVariables();
        final Map<String, String> responseVariables = new HashMap<>();
        final String responseType = requestConfig.getResponse().getType();

        variables.forEach((key, value) -> {
            addVariable(response, responseVariables, responseType, key, value);
        });

        return responseVariables;
    }

    private void addVariable(String response, Map<String, String> responseVariables, String responseType, String key, String value) {
        final String foundValue = parsers.get(responseType).getValueFrom(response, key);
        responseVariables.put("@".concat(value), foundValue);
    }
}