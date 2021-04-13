package beans.builders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import beans.SiteSettings;

public class SiteSettingsBuilder implements BeanBuilder<SiteSettings> {
	private static final String ERROR_READING_FILE_LOG = "Error reading file ";

	private static Logger logger = Logger.getLogger(SiteSettingsBuilder.class);

	private SiteSettings siteSettings = new SiteSettings();

	public SiteSettingsBuilder setDeliveryUrl(String deliveryUrl) {
		siteSettings.setDeliveryUrl(deliveryUrl);
		return this;
	}

	public SiteSettingsBuilder setName(String name) {
		siteSettings.setName(name);
		return this;
	}

	public SiteSettingsBuilder setLogo(byte[] logo) {
		siteSettings.setLogo(logo);
		return this;
	}

	public SiteSettingsBuilder setLogo(Path imagePath) throws SiteSettingsBuilderException {
		try {
			siteSettings.setLogo(Files.readAllBytes(imagePath));
		} catch (IOException e) {
			logger.error(ERROR_READING_FILE_LOG + imagePath.toString(), e);
			throw new SiteSettingsBuilderException(e);
		}

		return this;
	}

	@Override
	public SiteSettings build() {
		return siteSettings;
	}
}
