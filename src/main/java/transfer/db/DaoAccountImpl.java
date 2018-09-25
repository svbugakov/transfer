package transfer.db;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(DaoAccountImpl.class);

    @Inject
    public void setDataSource(final DataSource dt) {
        this.dataSource = dt;
    }

    @Override
    public List<Account> getAccounts() {
        logger.debug("trying get list of accounts...");
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
            logger.error("Error trying to get list of accounts", e);
        }
        logger.debug("finish list of accounts...");
        return accounts;
    }

    @Override
    public Account getAccount(String acc) {
        logger.debug("trying to get account = {}", acc);
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
                } else {
                    logger.debug("No found account = {}", acc);
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Error trying to get account = {}", acc, e);
        }
        logger.debug("finish get account = {}", acc);
        return account;
    }

    private void rollback(Connection con) {
        try {
            logger.debug("roolback transaction...");
            con.rollback();
        } catch (Exception er) {
            logger.error("Could not rollback bad tarnsfer operation!", er);
        }
    }

    private void handleTransact(PreparedStatement st, String acc, BigDecimal sum, boolean from)
            throws SQLException, NotCorrectSumTransaction, NoFoundAccountForTransaction {
        st.setString(1, acc);
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                BigDecimal bal = rs.getBigDecimal(2);
                if (from) {
                    if ((bal.compareTo(sum) >= 0)) {
                        rs.updateBigDecimal(2, bal.add(sum.negate()));
                        rs.updateRow();
                    } else {
                        throw new NotCorrectSumTransaction(String.format("The balance %1$s of the account is less than the transfer amount", acc));
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
        logger.debug("trying transfer money from {} " +
                "to {}", from, to);
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
                logger.debug("No correct parametrs for transaction");
            } catch (Exception e) {
                rollback(con);
                logger.error("Error for transfer...", e);
                result = new Result(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
            }
            con.setAutoCommit(true);
        } catch (Exception e) {
            logger.error("Error for transfer", e);
            result = new Result(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
        logger.debug("transfer money from {} " +
                "to {} finish", from, to);
        return result;
    }
}
