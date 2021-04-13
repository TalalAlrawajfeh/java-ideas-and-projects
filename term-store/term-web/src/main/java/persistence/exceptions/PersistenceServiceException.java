package persistence.exceptions;

public class PersistenceServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public PersistenceServiceException() {
		super();
	}

	public PersistenceServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceServiceException(String message) {
		super(message);
	}

	public PersistenceServiceException(Throwable cause) {
		super(cause);
	}
}
