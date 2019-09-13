package br.com.obonaldo.simplehttptest.gateways.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component("JSON")
@RequiredArgsConstructor
public class JsonResponseParser implements ResponseParser {

    private final ObjectMapper mapper;

    @Override
    public String getValueFrom(String response, String node) {
        //TODO pega apenas valores na raiz do json
        String value = "";
        try {
            final JsonNode jsonNode = mapper.readTree(response);
            value = jsonNode.get(node).asText();
        } catch (IOException e) {
            //TODO improve error handling
            log.error(e.getMessage());
        }
        return value;
    }
}