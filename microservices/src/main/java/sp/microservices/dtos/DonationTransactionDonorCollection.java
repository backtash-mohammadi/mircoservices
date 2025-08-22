package sp.microservices.dtos;

import java.util.List;


public class DonationTransactionDonorCollection {
    private List<DonationTransaction> items;

    public DonationTransactionDonorCollection() {}
    public DonationTransactionDonorCollection(List<DonationTransaction> items) { this.items = items; }

    public List<DonationTransaction> getItems() { return items; }
    public void setItems(List<DonationTransaction> items) { this.items = items; }
}
