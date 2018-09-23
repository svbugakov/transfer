package transfer.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.aopalliance.intercept.Invocation;
import transfer.service.OperTransAccounts;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

public class RestClient {
    private Client client;

    public RestClient() {
        client = Client.create();
    }
    public static void main(String[] args) {
        String acc = "40817000000000000001";

        RestClient rest = new RestClient();
        //rest.getAccInfo(acc);
        //rest.getAccInfoAll();
        rest.transferMoney();
    }

    private void getAccInfo(String acc) {
        WebResource webResource = client
                .resource("http://localhost:8084/rest/account/getAcc/" + acc);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        printResult(response);
    }

    private void getAccInfoAll() {
        WebResource webResource = client
                .resource("http://localhost:8084/rest/account/getAccs/");

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        printResult(response);
    }

    private void transferMoney() {
        OperTransAccounts oper = new OperTransAccounts();
        oper.setFrom("40817000000000000001");
        oper.setTo("40817000000000000003");
        oper.setSum(new BigDecimal(100));

        WebResource webResource = client
                .resource("http://localhost:8084/rest/account/transfer");

        ClientResponse response = webResource.accept("application/json")
                .type("application/json").put(ClientResponse.class, oper);

        printResult(response);
    }

    private void printResult(ClientResponse response) {
        String output = response.getEntity(String.class);
        int code = response.getStatus();

        System.out.println("Output from Server .... \n");
        if (code != 200)
            System.out.println("Failed : HTTP error code : " + code);
        System.out.println(output);
    }
}
