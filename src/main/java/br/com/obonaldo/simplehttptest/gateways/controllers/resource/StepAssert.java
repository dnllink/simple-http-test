package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import lombok.Data;

@Data
public class StepAssert {
    private String node;
    private String expectedValue;
    private String actualValue;
    private String status;

    public StepAssert(final String node, final String expectedValue, final String actualValue) {
        this.node = node;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
        this.status = expectedValue.equals(actualValue) ? StepResult.PASSED : StepResult.FAILED;
    }
}