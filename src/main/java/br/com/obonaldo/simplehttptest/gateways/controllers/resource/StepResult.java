package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StepResult {

    public static final String PASSED = "PASSED";
    public static final String FAILED = "FAILED";

    private String name;

    @Setter
    private String error;

    private List<StepAssert> asserts;

    @Setter
    private Map<String, String> variables;

    public StepResult(final String name) {
        this.name = name;
        this.asserts = new ArrayList<>();
        this.variables = new HashMap<>();
    }

    public void addAssert(final String node, final String expectedValue, final String actualValue ) {
        this.asserts.add(new StepAssert(node, expectedValue, actualValue));
    }

    public String getStatus() {
        return asserts.stream()
                .anyMatch(stepAssert -> FAILED.equals(stepAssert.getStatus()))
        ? FAILED : PASSED;
    }
}