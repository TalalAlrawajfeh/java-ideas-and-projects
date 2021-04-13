package usecases.exceptions;

public class SiteValidationException extends Exception {
	private static final long serialVersionUID = -2475211649751727018L;

	private final SiteValidationExceptionCause siteValidationExceptionCause;

	public SiteValidationException(SiteValidationExceptionCause siteValidationExceptionCause) {
		super();
		this.siteValidationExceptionCause = siteValidationExceptionCause;
	}

	public SiteValidationException() {
		super();
		siteValidationExceptionCause = SiteValidationExceptionCause.OTHER;
	}

	public SiteValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
		siteValidationExceptionCause = SiteValidationExceptionCause.OTHER;
	}

	public SiteValidationException(String message, Throwable cause) {
		super(message, cause);
		siteValidationExceptionCause = SiteValidationExceptionCause.OTHER;
	}

	public SiteValidationException(String message) {
		super(message);
		siteValidationExceptionCause = SiteValidationExceptionCause.OTHER;
	}

	public SiteValidationException(Throwable cause) {
		super(cause);
		siteValidationExceptionCause = SiteValidationExceptionCause.OTHER;
	}

	public SiteValidationExceptionCause getSiteValidationExceptionCause() {
		return siteValidationExceptionCause;
	}

	public enum SiteValidationExceptionCause {
		INVALID_NAME, INVALID_URI, OTHER
	}
}
