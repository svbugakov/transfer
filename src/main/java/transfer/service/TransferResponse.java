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
            ErrorResponse resp = new ErrorResponse();
            resp.setErrorCode(errorMessage);
            return Response.status(status).entity(resp).
                    type(MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
}
