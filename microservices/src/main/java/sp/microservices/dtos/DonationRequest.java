package sp.microservices.dtos;

import java.math.BigDecimal;

public class DonationRequest {
    private String donor;
    private String recipient;
    private BigDecimal amount;

    public DonationRequest() {}

    public DonationRequest(String donor, String recipient, BigDecimal amount) {
        this.donor = donor;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getDonor() { return donor; }
    public void setDonor(String donor) { this.donor = donor; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

