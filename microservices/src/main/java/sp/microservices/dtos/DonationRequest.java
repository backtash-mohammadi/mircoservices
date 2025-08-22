package sp.microservices.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRequest {
    private String donor;
    private String recipient;
    private BigDecimal amount;

}

