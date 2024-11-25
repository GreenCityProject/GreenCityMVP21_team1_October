package greencity.exception.exceptions;

/**
 * Exception that we get when user trying to save place with bad parameters.
 */
public class BadPlaceRequestException extends RuntimeException {
    /**
     * Constructor for BadPlaceRequestException.
     */
    public BadPlaceRequestException(String message) {
        super(message);
    }
}
