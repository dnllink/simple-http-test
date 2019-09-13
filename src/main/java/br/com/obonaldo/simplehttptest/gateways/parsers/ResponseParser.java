package br.com.obonaldo.simplehttptest.gateways.parsers;

public interface ResponseParser {
    String getValueFrom(String response, String node);
}