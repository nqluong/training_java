package AssignmentJavaCore.exception;

public class ProducerException extends MyCustomException{
    public ProducerException(String message) {
        super(message, "PRODUCER_ERROR");
    }

    public ProducerException(String message, Throwable cause) {
        super(message, "PRODUCER_ERROR", cause);
    }
}
