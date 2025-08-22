package sp.microservices.dtos;

import java.math.BigDecimal;

public class BalanceChange {
    private String username;
    private BigDecimal delta;

    public BalanceChange() {}

    public BalanceChange(String username, BigDecimal delta) {
        this.username = username;
        this.delta = delta;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public BigDecimal getDelta() { return delta; }
    public void setDelta(BigDecimal delta) { this.delta = delta; }
}

