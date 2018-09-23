package transfer.db;

import com.google.inject.Inject;
import transfer.model.Account;
import transfer.service.OperTransAccounts;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class DaoAccountImpl implements DaoAccount {

    private DataSource dataSource;

    @Inject
    public void setDataSource(final DataSource dt) {
        this.dataSource = dt;
    }

    //COnnection pull!!!!!!!!!!!!
    @Override
    public BigDecimal getBalance() {
        BigDecimal balance = null;

        try (Connection con = dataSource.getConnection()) {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("select sum(bal) from account")) {
                if (rs.next()) {
                    balance = rs.getBigDecimal(1);
                }

                System.out.println("balance1:" + balance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        final String SQL = "select a.acc, " +
                "a.bal " +
                "from account a";

        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL)) {
            while (rs.next()) {
                Account account = new Account();
                account.setAcc(rs.getString(1));
                account.setBalance(rs.getBigDecimal(2));
                accounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account getAccount(String acc) {
        Account account = null;
        final String SQL = "select a.acc, " +
                "a.bal " +
                "from account a where acc = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement st = con.prepareStatement(SQL);
        ) {
            st.setString(1, acc);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    account = new Account();
                    account.setAcc(rs.getString(1));
                    account.setBalance(rs.getBigDecimal(2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public void transferSum(OperTransAccounts oper) {
        Account account = null;
        final String SQL = "update account a set a.bal = a.bal + ? where acc = ?";

        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(SQL)) {
                con.setAutoCommit(false);
                con.setTransactionIsolation(TRANSACTION_READ_COMMITTED);
                st.setBigDecimal(1, oper.getSum().negate());
                st.setString(2, oper.getFrom());
                st.executeUpdate();
                st.setBigDecimal(1, oper.getSum());
                st.setString(2, oper.getTo());
                st.executeUpdate();
                System.out.println("update  rows");
                con.commit();
            } catch (Exception e) {
                try {
                    con.rollback();
                } catch (Exception er) {
                    System.out.println("Could not rollback bad tarnsfer operation!");
                }
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        oper.setResult(String.format("Money transferred from the account %s to %s successfully",
                oper.getFrom(), oper.getTo()));
    }
}
