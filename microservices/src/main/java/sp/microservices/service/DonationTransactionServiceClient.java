package sp.microservices.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class DonationTransactionServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DonationTransactionServiceClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    public DonationTransactionServiceClient(RestTemplate restTemplate,
                                            @Value("${donation.tx.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /**
     * Возвращает общую сумму пожертвований донора, устойчиво к формату ответа:
     * - [ { amount: 10 }, ... ]
     * - { items: [ { amount: 10 }, ... ] }
     * - { transactions: [ ... ] }
     * Поддерживает поля суммы: amount | value | euroAmount.
     */
    public BigDecimal getTotalByDonor(String donor) {
        try {
            URI uri = URI.create(baseUrl + "/transactions/donor/" + donor);
            var request = RequestEntity.get(uri).build();
            var response = restTemplate.exchange(request, String.class);
            String body = response.getBody();
            if (body == null || body.isBlank()) {
                log.warn("Empty response from donation-transaction-service for donor={}", donor);
                return BigDecimal.ZERO;
            }

            JsonNode root = mapper.readTree(body);
            List<JsonNode> txNodes = extractTransactionsArray(root);

            BigDecimal total = BigDecimal.ZERO;
            for (JsonNode tx : txNodes) {
                Optional<BigDecimal> amount = readAmount(tx);
                if (amount.isPresent()) {
                    total = total.add(amount.get());
                } else {
                    log.debug("Transaction without recognizable amount field: {}", tx.toString());
                }
            }
            return total;
        } catch (Exception ex) {
            log.error("Failed to fetch/parse transactions for donor={}: {}", donor, ex.getMessage());
            return BigDecimal.ZERO;
        }
    }

    private List<JsonNode> extractTransactionsArray(JsonNode root) {
        List<JsonNode> list = new ArrayList<>();
        if (root.isArray()) {
            root.forEach(list::add);
            return list;
        }
        // пробуем популярные обёртки
        for (String key : new String[]{"items", "transactions", "data", "content"}) {
            JsonNode arr = root.get(key);
            if (arr != null && arr.isArray()) {
                arr.forEach(list::add);
                return list;
            }
        }
        // если это объект-одиночка, попробуем пройти по всем полям и найти первый массив объектов
        Iterator<String> it = root.fieldNames();
        while (it.hasNext()) {
            String k = it.next();
            JsonNode v = root.get(k);
            if (v != null && v.isArray()) {
                v.forEach(list::add);
                return list;
            }
        }
        return list; // пусто — вернётся 0
    }

    private Optional<BigDecimal> readAmount(JsonNode tx) {
        for (String fld : new String[]{"amount", "value", "euroAmount"}) {
            JsonNode n = tx.get(fld);
            if (n == null || n.isNull()) continue;

            if (n.isNumber()) {
                return Optional.of(n.decimalValue());
            }
            if (n.isTextual()) {
                try {
                    return Optional.of(new BigDecimal(n.asText().trim()));
                } catch (NumberFormatException ignore) { /* fallthrough */ }
            }
        }
        return Optional.empty();
    }
}
