package transfer.model;

import javax.ws.rs.core.Response.Status;

public class Result {

    private String message;
    private Status status;

    public Result() {
    }

    public Result(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
