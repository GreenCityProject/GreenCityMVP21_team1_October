package greencity.exception.exceptions;

public class CommentValidationException extends RuntimeException {
    public CommentValidationException() {
        super();
    }

    public CommentValidationException(String message) {
        super(message);
    }
}
