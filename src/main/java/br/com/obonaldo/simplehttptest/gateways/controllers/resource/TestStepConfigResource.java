package br.com.obonaldo.simplehttptest.gateways.controllers.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestStepConfigResource {
    private String name;
    private String url;
    private String method;
    private Map<String, String> queryParams;
    private Map<String, String> headers;
    private String payload;
    private List<String> pathParams;
    private ResponseResource response;

    @JsonIgnore
    public boolean hasRetry() {
        return nonNull(response) && !isEmpty(response.getRetries());
    }

    @JsonIgnore
    public boolean hasAsserts() {
        return nonNull(response) && !isEmpty(response.getAsserts());
    }

    public void resolvePathParams(final Map<String, String> variables) {
        if (!isEmpty(pathParams) && !isEmpty(variables)) {
            this.pathParams = pathParams.stream()
                    .map(param ->
                        param.startsWith("@") ? variables.get(param) : param
                    ).collect(Collectors.toList());
        }
    }
}