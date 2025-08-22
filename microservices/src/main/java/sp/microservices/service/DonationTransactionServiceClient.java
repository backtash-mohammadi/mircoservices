package sp.microservices.service;

import sp.microservices.dtos.DonationTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Component
public class DonationTransactionServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public DonationTransactionServiceClient(RestTemplate restTemplate,
                                            @Value("${donation.tx.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /**
     * Ожидаем, что апстрим даёт: GET {base}/transactions/donor/{donor} -> JSON-массив транзакций.
     */
    public List<DonationTransaction> getTransactionsByDonor(String donor) {
        try {
            URI uri = URI.create(baseUrl + "/transactions/donor/" + donor);
            var request = RequestEntity.get(uri).build();
            var response = restTemplate.exchange(
                    request,
                    new ParameterizedTypeReference<List<DonationTransaction>>() {}
            );
            var body = response.getBody();
            return body != null ? body : Collections.emptyList();
        } catch (Exception ex) {
            // На проде — логирование/метрики. Здесь — просто пустой список, чтобы не упасть.
            return Collections.emptyList();
        }
    }
}

