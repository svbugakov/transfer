package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;
import transfer.db.DaoAccountImpl;
import transfer.db.DataSourceModule;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Path("/account")
public class TransferAccount {
    private Acc acc;
    private DataSource dataSource;
    private DaoAccount daoAccount;

    @Inject
    @Named("red")
    private String service;


    @Inject
    public void setService(Acc acc) {
        this.acc = acc;
    }

    @Inject
    public void setDataSource(final DataSource dt) {
        this.dataSource = dt;
    }

    @Inject
    public void setDaoAccount(final DaoAccount daoAccount) {
        this.daoAccount = daoAccount;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String isExistsAccount() {

        System.out.println("yyyyyyyyyyyyyyyyy");

        Connection cn = null;

        try {
            try {
                cn = dataSource.getConnection();
                Statement st = cn.createStatement();
                st.execute("create table account(bal DECIMAL(20, 2))");
                st.execute("insert into account(bal) values(198)");
                System.out.println("balance:" + daoAccount.getBalance());
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Use the connection
        } finally {
            try {
                cn.close();
            } catch (Exception e) {
            }
        }
        return "yes account exists" + acc.say() + ":" + service;
    }
}
