package greencity.exception.exceptions;

public class CannotLikeYourOwnCommentException extends RuntimeException {
    public CannotLikeYourOwnCommentException() {
        super();
    }
    public CannotLikeYourOwnCommentException(String message) {
        super(message);
    }
}
