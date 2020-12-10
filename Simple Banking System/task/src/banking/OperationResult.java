package banking;

public class OperationResult {
    public final Status status;
    public final String statusText;

    public OperationResult(Status status, String statusText) {
        this.status = status;
        this.statusText = statusText;
    }

    public static OperationResult failure(String error) {
        return new OperationResult(Status.FAILURE, error);
    }

    public static OperationResult success(String message) {
        return new OperationResult(Status.SUCCESS, message);
    }

    public static OperationResult success() {
        return new OperationResult(Status.SUCCESS, "success");
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isFailure() {
        return status == Status.FAILURE;
    }
}
