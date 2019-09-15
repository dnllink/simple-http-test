package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import lombok.Getter;

import java.util.List;

@Getter
public class TestConfigResource {
    private String name;
    private List<TestStepConfigResource> steps;
}