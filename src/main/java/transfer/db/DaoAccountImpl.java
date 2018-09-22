package transfer.db;

import com.google.inject.Inject;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
}
