package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;


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

    private DaoAccount daoAccount;

    @Inject
    @Named("red")
    private String service;


    @Inject
    public void setService(Acc acc) {
        this.acc = acc;
    }


    @Inject
    public void setDaoAccount(final DaoAccount daoAccount) {
        this.daoAccount = daoAccount;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String isExistsAccount() {

        System.out.println("yyyyyyyyyyyyyyyyy");


        System.out.println("balance:" + daoAccount.getBalance());


        return "yes account exists" + acc.say() + ":" + service;
    }
}
