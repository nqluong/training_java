package AssignmentJavaCore.exception;

public class ConsumerException extends MyCustomException{
    public ConsumerException(String message) {
        super(message, "CONSUMER_ERROR");
    }
    public ConsumerException(String message, Throwable cause) {
        super(message, "CONSUMER_ERROR", cause);
    }
}
