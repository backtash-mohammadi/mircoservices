package sp.microservices.service;

import sp.microservices.dtos.BalanceChange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sp.microservices.dtos.User;

import java.net.URI;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserServiceClient(RestTemplate restTemplate,
                             @Value("${user.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public User getUser(String username) {
        try {
            var response = restTemplate.getForEntity(baseUrl + "/users/" + username, User.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean applyBalanceChange(BalanceChange change) {
        try {
            URI uri = URI.create(baseUrl + "/users/" + change.getUsername() + "/balance-change");
            var request = new HttpEntity<>(change);
            restTemplate.postForEntity(uri, request, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
