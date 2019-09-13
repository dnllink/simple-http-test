package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResource {
    private String type;
    private Map<String, String> retries;
    private Map<String, String> variables;
    private Map<String, String> asserts;
}