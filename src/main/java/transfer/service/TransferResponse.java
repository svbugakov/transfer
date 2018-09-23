package transfer.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TransferResponse<T> {
    private T entity;
    private String errorMessage;

    public TransferResponse(T entity, String errorMessage) {
        this.entity = entity;
        this.errorMessage = errorMessage;
    }

    public Response getResponse() {
        if (entity == null) {
            ErrorResponse resp = new ErrorResponse();
            resp.setErrorCode(errorMessage);
            return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(resp).
                    type(MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
}
