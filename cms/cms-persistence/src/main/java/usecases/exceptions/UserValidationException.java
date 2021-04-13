package usecases.exceptions;

public class UserValidationException extends Exception {
	private static final long serialVersionUID = 2830885225551242621L;

	private final UserValidationExceptionCause userValidationExceptionCause;

	public UserValidationException(UserValidationExceptionCause userValidationExceptionCause) {
		super();
		this.userValidationExceptionCause = userValidationExceptionCause;
	}

	public UserValidationException() {
		super();
		userValidationExceptionCause = UserValidationExceptionCause.OTHER;
	}

	public UserValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
		userValidationExceptionCause = UserValidationExceptionCause.OTHER;
	}

	public UserValidationException(String message, Throwable cause) {
		super(message, cause);
		userValidationExceptionCause = UserValidationExceptionCause.OTHER;
	}

	public UserValidationException(String message) {
		super(message);
		userValidationExceptionCause = UserValidationExceptionCause.OTHER;
	}

	public UserValidationException(Throwable cause) {
		super(cause);
		userValidationExceptionCause = UserValidationExceptionCause.OTHER;
	}

	public UserValidationExceptionCause getValidationExceptionCause() {
		return userValidationExceptionCause;
	}

	public enum UserValidationExceptionCause {
		INVALID_FULLNAME, INVALID_USERNAME, OTHER
	}
}
