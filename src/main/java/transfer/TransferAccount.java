package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;
import transfer.model.Account;
import transfer.service.ErrorResponse;
import transfer.service.TransferResponse;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/account")
public class TransferAccount {
    private DaoAccount daoAccount;

    @Inject
    @Named("red")
    private String service;

    @Inject
    public void setDaoAccount(final DaoAccount daoAccount) {
        this.daoAccount = daoAccount;
    }

    @GET
    @Path("/getAccs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> getAllAccounts() {
        System.out.println("Start get all accounts");
        return daoAccount.getAccounts();
    }

    @GET
    @Path("/getAcc/{acc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@PathParam("acc") String acc) {
        System.out.println("Start get account");
        Account account = daoAccount.getAccount(acc);
        return new TransferResponse<Account>(account, "No found account: "+acc)
                .getResponse();
    }
}
