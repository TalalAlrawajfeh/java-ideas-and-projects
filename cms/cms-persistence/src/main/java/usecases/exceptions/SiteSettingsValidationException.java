package usecases.exceptions;

public class SiteSettingsValidationException extends Exception {
	private static final long serialVersionUID = 5659273274426617893L;

	private final SiteSettingsValidationExceptionCause siteSettingsValidationExceptionCause;

	public SiteSettingsValidationException(SiteSettingsValidationExceptionCause siteSettingsValidationExceptionCause) {
		super();
		this.siteSettingsValidationExceptionCause = siteSettingsValidationExceptionCause;
	}

	public SiteSettingsValidationException() {
		super();
		siteSettingsValidationExceptionCause = SiteSettingsValidationExceptionCause.OTHER;
	}

	public SiteSettingsValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		siteSettingsValidationExceptionCause = SiteSettingsValidationExceptionCause.OTHER;
	}

	public SiteSettingsValidationException(String message, Throwable cause) {
		super(message, cause);
		siteSettingsValidationExceptionCause = SiteSettingsValidationExceptionCause.OTHER;
	}

	public SiteSettingsValidationException(String message) {
		super(message);
		siteSettingsValidationExceptionCause = SiteSettingsValidationExceptionCause.OTHER;
	}

	public SiteSettingsValidationException(Throwable cause) {
		super(cause);
		siteSettingsValidationExceptionCause = SiteSettingsValidationExceptionCause.OTHER;
	}

	public SiteSettingsValidationExceptionCause getSiteSettingsValidationExceptionCause() {
		return siteSettingsValidationExceptionCause;
	}

	public enum SiteSettingsValidationExceptionCause {
		INVALID_DELIVERY_URL, INVALID_NAME, INVALID_LOGO, OTHER
	}
}
