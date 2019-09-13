package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestConfigResource {
    private String name;
    private List<TestStepConfigResource> steps;
}