package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestResult {
    private String name;
    private List<StepResult> steps;

    public TestResult(final String name) {
        steps = new ArrayList<>();
        this.name = name;
    }

    public void addStep(final StepResult stepResult) {
        this.steps.add(stepResult);
    }
}