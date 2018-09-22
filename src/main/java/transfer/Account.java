package transfer;

import com.google.inject.Singleton;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class Account implements Acc {
    private String acc;
    private BigDecimal balance;

    public String say() {
        return "asasdasdsad";
    }

    @Override
    public String getAcc() {
        return acc;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public void setAcc(String acc) {
        this.acc = acc;
    }
}
