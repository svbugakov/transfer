package transfer.db;

import transfer.Acc;

import java.math.BigDecimal;
import java.util.List;

public interface DaoAccount {
    BigDecimal getBalance();
    List<Acc> getAccounts();
}
