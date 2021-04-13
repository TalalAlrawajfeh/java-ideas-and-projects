package persistence.jpa.exceptions;

public class JPAEntityPersistenceProviderException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JPAEntityPersistenceProviderException() {
		super();
	}

	public JPAEntityPersistenceProviderException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JPAEntityPersistenceProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public JPAEntityPersistenceProviderException(String message) {
		super(message);
	}

	public JPAEntityPersistenceProviderException(Throwable cause) {
		super(cause);
	}
}
