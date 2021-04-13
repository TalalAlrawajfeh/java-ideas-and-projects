package usecases.exceptions;

public class InvalidUserException extends Exception {
	private static final long serialVersionUID = 8106985756540152268L;

	private final InvalidUserExceptionCause invalidUserCause;

	public InvalidUserException(InvalidUserExceptionCause invalidUserCause) {
		super();
		this.invalidUserCause = invalidUserCause;
	}

	public InvalidUserException() {
		super();
		invalidUserCause = InvalidUserExceptionCause.OTHER;
	}

	public InvalidUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
		invalidUserCause = InvalidUserExceptionCause.OTHER;
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
		invalidUserCause = InvalidUserExceptionCause.OTHER;
	}

	public InvalidUserException(String message) {
		super(message);
		invalidUserCause = InvalidUserExceptionCause.OTHER;
	}

	public InvalidUserException(Throwable cause) {
		super(cause);
		invalidUserCause = InvalidUserExceptionCause.OTHER;
	}

	public InvalidUserExceptionCause getInvalidUserCause() {
		return invalidUserCause;
	}

	public enum InvalidUserExceptionCause {
		USER_NOT_FOUND, INVALID_PASSWORD, OTHER;
	}
}
