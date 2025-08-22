package sp.microservices.dtos;

import java.math.BigDecimal;

public class DonationTransaction {
    private String id;         // может отсутствовать — не критично
    private String donor;
    private String recipient;  // если есть
    private BigDecimal amount; // сумма в евро

    public DonationTransaction() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDonor() { return donor; }
    public void setDonor(String donor) { this.donor = donor; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

