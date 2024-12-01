package greencity.exception.exceptions;

public class FavoritePlaceNotFoundException extends RuntimeException {
    public FavoritePlaceNotFoundException(String message) {
        super(message);
    }
    public FavoritePlaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public FavoritePlaceNotFoundException() {}
}
