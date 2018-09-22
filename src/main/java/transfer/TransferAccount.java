package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;


import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
    @Path("/account")
    @Produces(MediaType.TEXT_PLAIN)
    public String isExistsAccount() {

        System.out.println("yyyyyyyyyyyyyyyyy");


        System.out.println("balance:" + daoAccount.getBalance());


        return "yes account exists" + acc.say() + ":" + service;
    }

    @GET
    @Path("/getAccs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Account getAllAccounts() {
        System.out.println("Start get all accounts");
        Account a = new Account();
        a.setAcc("adsad0");

        return a;
    }
}
