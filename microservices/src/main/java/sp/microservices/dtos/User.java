package sp.microservices.dtos;


import java.math.BigDecimal;

public record User(String username, BigDecimal balance) {
    public User {
        if (balance == null) balance = BigDecimal.ZERO;
    }
}

