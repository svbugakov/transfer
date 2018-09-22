package transfer;

import java.math.BigDecimal;

public interface Acc {
    String say();

    String getAcc();

    BigDecimal getBalance();

    void setBalance(BigDecimal balance);

    void setAcc(String acc);
}
