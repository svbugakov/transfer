package transfer.db;

import transfer.model.Account;
import transfer.service.OperTransAccounts;

import java.math.BigDecimal;
import java.util.List;

public interface DaoAccount {
    //Ecxeption sss
    BigDecimal getBalance();

    List<Account> getAccounts();

    Account getAccount(String acc);

    void transferSum(OperTransAccounts oper);
}
