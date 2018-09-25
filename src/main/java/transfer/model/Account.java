package transfer.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class Account {
    private String acc;
    private BigDecimal balance;

    public String getAcc() {
        return acc;
    }


    public BigDecimal getBalance() {
        return balance;
    }


    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public void setAcc(String acc) {
        this.acc = acc;
    }
}
