package transfer.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TransferResponse<T> {
    private T entity;
    private String errorMessage;
    private Response.Status status;

    public TransferResponse(T entity, String errorMessage, Response.Status status) {
        this.entity = entity;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public Response getResponse() {
        if (errorMessage != null) {
            return Response.status(status).entity(errorMessage).
                    type(MediaType.TEXT_PLAIN).build();
        }
        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
}
