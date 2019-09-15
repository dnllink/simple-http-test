package br.com.obonaldo.simplehttptest.gateways.parsers;

import java.util.Optional;

public interface ResponseParser {
    Optional<String> getValueFrom(String response, String node);
}