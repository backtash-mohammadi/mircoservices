package sp.microservices;

import sp.microservices.dtos.TotalDonationsReponse;
import sp.microservices.service.DonationTransactionServiceClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DonationController {

    private final DonationTransactionServiceClient txClient;

    public DonationController(DonationTransactionServiceClient txClient) {
        this.txClient = txClient;
    }

    @GetMapping("/total-donations/{donor}")
    public TotalDonationsReponse totalDonations(@PathVariable("donor") String donor) {
        BigDecimal total = txClient.getTotalByDonor(donor);
        return new TotalDonationsReponse(donor, total);
    }
}