package AssignmentJavaCore.exception;

public class MyRuntimeException extends RuntimeException {
    private final String errorCode;
    private final long timestamp;

    public MyRuntimeException(String message) {
        super(message);
        this.errorCode = "RUNTIME_ERROR";
        this.timestamp = System.currentTimeMillis();
    }

    public MyRuntimeException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    public MyRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "RUNTIME_ERROR";
        this.timestamp = System.currentTimeMillis();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
