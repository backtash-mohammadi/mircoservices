package sp.microservices;

import sp.microservices.dtos.DonationTransaction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sp.microservices.dtos.TotalDonationsReponse;
import sp.microservices.service.DonationTransactionServiceClient;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DonationController {

    private final DonationTransactionServiceClient txClient;

    public DonationController(DonationTransactionServiceClient txClient) {
        this.txClient = txClient;
    }

    /**
     * Task 1: вернуть сумму всех пожертвований done by donor.
     * GET /total-donations/{donor}
     *
     * Ответ:
     * {
     *   "donor": "alex",
     *   "amount": 60
     * }
     */
    @GetMapping("/total-donations/{donor}")
    public TotalDonationsReponse totalDonations(@PathVariable("donor") String donor) {
        List<DonationTransaction> transactions = txClient.getTransactionsByDonor(donor);

        BigDecimal total = transactions.stream()
                .map(DonationTransaction::getAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TotalDonationsReponse(donor, total);
    }
}
