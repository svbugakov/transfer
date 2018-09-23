package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;
import transfer.model.Account;
import transfer.service.ErrorResponse;
import transfer.service.OperTransAccounts;
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
        String messageerror = null;
        Status status = Status.OK;
        if (account == null) {
            messageerror = "No found account: " + acc;
            status = Status.NOT_FOUND;
        }
        return new TransferResponse<Account>(account, messageerror, status).getResponse();
    }


    @PUT
    @Path("/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferSumBetweenAccount(OperTransAccounts oper) {
        System.out.println("Start upd account from = " + oper.getFrom() + " on sum = " + oper.getSum());
        Account accountFrom = daoAccount.getAccount(oper.getFrom());
        Account accountTo = daoAccount.getAccount(oper.getTo());

        if (accountFrom == null) {
            return new TransferResponse<Account>(accountFrom, "account From no found in base",
                    Status.NOT_FOUND).getResponse();
        }
        if (accountTo == null) {
            return new TransferResponse<Account>(accountFrom, "account To no found in base",
                    Status.NOT_FOUND).getResponse();
        }
        daoAccount.transferSum(oper);
        return Response.ok(oper.getResult(), MediaType.APPLICATION_JSON).build();
    }
}
