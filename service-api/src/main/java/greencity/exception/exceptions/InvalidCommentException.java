package greencity.exception.exceptions;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException() {
        super();
    }

    public InvalidCommentException(String message) {
        super(message);
    }
}
