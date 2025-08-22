package sp.microservices.dtos;

import java.math.BigDecimal;

public class TotalDonationsReponse {
    private String donor;
    private BigDecimal amount;

    public TotalDonationsReponse() {}

    public TotalDonationsReponse(String donor, BigDecimal amount) {
        this.donor = donor;
        this.amount = amount;
    }

    public String getDonor() { return donor; }
    public void setDonor(String donor) { this.donor = donor; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
