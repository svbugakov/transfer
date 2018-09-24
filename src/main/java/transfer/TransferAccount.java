package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import transfer.db.DaoAccount;
import transfer.model.Account;
import transfer.model.Result;
import transfer.service.TransferResponse;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.math.BigDecimal;
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
    public Response transferSumBetweenAccount(@FormParam("from") String from,
                                              @FormParam("to") String to,
                                              @FormParam("sum") BigDecimal sum) {
        System.out.println("Start upd account from = " + from + " on sum = " + sum);
        Account accountFrom = daoAccount.getAccount(from);
        Account accountTo = daoAccount.getAccount(to);

        if (accountFrom == null) {
            return new TransferResponse<Account>(accountFrom, "account From no found in base",
                    Status.NOT_FOUND).getResponse();
        }
        if (accountTo == null) {
            return new TransferResponse<Account>(accountFrom, "account To no found in base",
                    Status.NOT_FOUND).getResponse();
        }
        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            return new TransferResponse<BigDecimal>(sum, "sum must be > 0",
                    Status.BAD_REQUEST).getResponse();
        }

        Result result = daoAccount.transferSum(accountFrom.getAcc(), accountTo.getAcc(), sum);
        return Response.status(result.getStatus()).entity(result.getMessage()).
                type(MediaType.APPLICATION_JSON).build();
    }
}
