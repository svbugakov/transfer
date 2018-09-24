package transfer.db;

import com.google.inject.Inject;
import transfer.db.exception.NoFoundAccountForTransaction;
import transfer.db.exception.NotCorrectSumTransaction;
import transfer.model.Account;
import transfer.model.Result;

import javax.sql.DataSource;
import javax.ws.rs.core.Response.Status;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class DaoAccountImpl implements DaoAccount {

    private DataSource dataSource;

    @Inject
    public void setDataSource(final DataSource dt) {
        this.dataSource = dt;
    }

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

    private void rollback(Connection con) {
        try {
            System.out.println("roolback transaction...");
            con.rollback();
        } catch (Exception er) {
            System.out.println("Could not rollback bad tarnsfer operation!");
        }
    }

    private void handleTransact(PreparedStatement st, String acc, BigDecimal sum, boolean from)
            throws SQLException, NotCorrectSumTransaction, NoFoundAccountForTransaction {
        st.setString(1, acc);
        try (ResultSet rs = st.executeQuery()) {
            System.out.println("try update acc" + acc + "server()" + "...");
            if (rs.next()) {
                BigDecimal bal = rs.getBigDecimal(2);
                System.out.println("old sum acc" + acc + "server()" + "=" + bal);
                if (from) {
                    if ((bal.compareTo(sum) >= 0)) {
                        rs.updateBigDecimal(2, bal.add(sum.negate()));
                        rs.updateRow();
                    } else {
                        throw new NotCorrectSumTransaction(String.format("The balance %1$s of the account is less than the transfer amount", from));
                    }
                } else {
                    rs.updateBigDecimal(2, bal.add(sum));
                    rs.updateRow();
                }
            } else {
                throw new NoFoundAccountForTransaction(String.format("No account %1$s was found for the transaction", from));
            }
        }
    }

    @Override
    public Result transferSum(String from, String to, BigDecimal sum) {
        final String SQL_SEL = "select acc, bal "
                + "from account where acc = ? for update";
        Result result = null;

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(TRANSACTION_READ_COMMITTED);
            try (PreparedStatement st = con.prepareStatement(SQL_SEL,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {

                if (from.compareTo(to) < 0) {
                    handleTransact(st, from, sum, true);
                    handleTransact(st, to, sum, false);
                } else {
                    handleTransact(st, to, sum, false);
                    handleTransact(st, from, sum, true);
                }

                con.commit();
                result = new Result(String.format("Money transferred from the account %s to %s " +
                        "successfully", from, to), Status.OK);
            } catch (NotCorrectSumTransaction | NoFoundAccountForTransaction e) {
                rollback(con);
                result = new Result(e.getMessage(), Status.BAD_REQUEST);
                //logger e
                System.err.println(e.getMessage() + " = ");
            } catch (Exception e) {
                rollback(con);
                // loogg e
                System.err.println(e.getMessage());
                result = new Result(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
            }
            con.setAutoCommit(true);
            System.out.println("finish server....");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = new Result(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
        return result;
    }
}
