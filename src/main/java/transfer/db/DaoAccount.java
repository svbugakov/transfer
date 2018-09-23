package transfer.db;

import transfer.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface DaoAccount {
    BigDecimal getBalance();
    List<Account> getAccounts();
}
