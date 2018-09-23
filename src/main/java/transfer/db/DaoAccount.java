package transfer.db;

import transfer.model.Account;
import transfer.model.Result;

import java.math.BigDecimal;
import java.util.List;

public interface DaoAccount {
    //Ecxeption sss
    BigDecimal getBalance();

    List<Account> getAccounts();

    Account getAccount(String acc);

    Result transferSum(Account from, Account to, BigDecimal sum);
}
