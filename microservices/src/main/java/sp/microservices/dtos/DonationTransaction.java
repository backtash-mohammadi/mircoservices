package sp.microservices.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DonationTransaction {
    private String id;         // может отсутствовать — не критично
    private String donor;
    private String recipient;  // если есть
    private BigDecimal amount; // сумма в евро

}

