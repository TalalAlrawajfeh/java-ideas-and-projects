package exceptions;

/**
 * Created by u624 on 3/24/17.
 */
public class UtilityException extends RuntimeException {
    public UtilityException() {
        /* default constructor */
    }

    public UtilityException(String message) {
        super(message);
    }

    public UtilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilityException(Throwable cause) {
        super(cause);
    }

    public UtilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
