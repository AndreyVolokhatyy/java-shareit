package src.main.java.ru.practicum.shareit.client;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class BaseClient {

    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, Map<String, String> headers) {
        return get(path, headers, null);
    }

    protected ResponseEntity<Object> get(String path, Map<String, String> headers, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, headers, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Map<String, String> headers, T body) {
        return post(path, headers, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Map<String, String> headers, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, headers, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, Map<String, String> headers, T body) {
        return put(path, headers, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path, Map<String, String> headers, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, headers, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Map<String, String> headers) {
        return patch(path, headers, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, Map<String, String> headers, T body) {
        return patch(path, headers, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Map<String, String> headers, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, headers, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    protected ResponseEntity<Object> delete(String path, Map<String, String> headers) {
        return delete(path, headers, null);
    }

    protected ResponseEntity<Object> delete(String path, Map<String, String> headers, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, headers, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Map<String, String> headers, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(headers));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders(Map<String, String> headers) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (headers != null) {
            if (headers.get("x-sharer-user-id") != null && !headers.get("x-sharer-user-id").isEmpty()) {
                header.set("x-sharer-user-id", headers.get("x-sharer-user-id"));
            } else {
                throw new IllegalArgumentException("Нет ИД пользователя");
            }
        }
        return header;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
