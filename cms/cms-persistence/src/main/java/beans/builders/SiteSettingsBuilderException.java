package beans.builders;

public class SiteSettingsBuilderException extends Exception {
	private static final long serialVersionUID = -727071443346026169L;

	public SiteSettingsBuilderException() {
		super();
	}

	public SiteSettingsBuilderException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SiteSettingsBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public SiteSettingsBuilderException(String message) {
		super(message);
	}

	public SiteSettingsBuilderException(Throwable cause) {
		super(cause);
	}
}
