package transfer;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class TransferAccount {
    private Acc acc;

    @Inject
    @Named("red")
    private String service;

    @Inject
    public void setService(Acc acc) {
        this.acc = acc;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String isExistsAccount() {

        System.out.println("yyyyyyyyyyyyyyyyy");
        return "yes account exists" + acc.say()+":"+service;
    }
}
