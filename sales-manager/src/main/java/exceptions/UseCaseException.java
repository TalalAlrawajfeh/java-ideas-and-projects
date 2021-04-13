package exceptions;

/**
 * Created by u624 on 3/24/17.
 */
public class UseCaseException extends Exception {
    public UseCaseException() {
        /* default constructor */
    }

    public UseCaseException(String message) {
        super(message);
    }

    public UseCaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UseCaseException(Throwable cause) {
        super(cause);
    }

    public UseCaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
