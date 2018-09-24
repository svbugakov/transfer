package transfer.db.exception;

public class NoFoundAccountForTransaction extends Exception {
    public NoFoundAccountForTransaction(String message) {
        super(message);
    }
}
