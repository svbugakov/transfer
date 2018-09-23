package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;
import transfer.model.Account;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/account")
public class TransferAccount {
    private Account acc;

    private DaoAccount daoAccount;

    @Inject
    @Named("red")
    private String service;


    @Inject
    public void setService(Account acc) {
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
    public List<Account> getAllAccounts() {
        System.out.println("Start get all accounts");
        return daoAccount.getAccounts();
    }
}
