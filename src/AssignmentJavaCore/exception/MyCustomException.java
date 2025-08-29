package AssignmentJavaCore.exception;

public class MyCustomException extends Exception{
    private final String errorCode;
    private final long timestamp;

    public MyCustomException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
        this.timestamp = System.currentTimeMillis();
    }

    public MyCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    public MyCustomException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
        this.timestamp = System.currentTimeMillis();
    }

    public MyCustomException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s at %d: %s",
                errorCode, getClass().getSimpleName(), timestamp, getMessage());
    }
}
