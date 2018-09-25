package transfer.db;

import transfer.model.Account;
import transfer.model.Result;

import java.math.BigDecimal;
import java.util.List;

public interface DaoAccount {
    List<Account> getAccounts();

    Account getAccount(String acc);

    Result transferSum(String from, String to, BigDecimal sum);
}
