package usecases;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import beans.SiteSettings;
import beans.builders.SiteSettingsBuilder;
import beans.builders.SiteSettingsBuilderException;
import entities.SiteSettingsEntity;
import persistence.SiteSettingsRepository;
import usecases.exceptions.SiteSettingsValidationException;
import usecases.exceptions.SiteSettingsValidationException.SiteSettingsValidationExceptionCause;
import utils.CopyUtil;

public class SiteSettingsUseCase {
	private static final String DELIVERY_URL_VALIDATION_REGEX = "/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static final String LOGO_IMAGE_VALIDATION_REGEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
	private static final String NAME_VALIDATION_REGEX = "[a-zA-Z\\s]{3,50}";

	@Autowired
	private SiteSettingsRepository siteSettingsRepository;

	public SiteSettings validateSiteSettings(String deliveryUrl, String name, String logoImagePath)
			throws SiteSettingsValidationException {

		if (!deliveryUrl.matches(DELIVERY_URL_VALIDATION_REGEX)) {
			throw new SiteSettingsValidationException(SiteSettingsValidationExceptionCause.INVALID_DELIVERY_URL);
		}

		if (!name.matches(NAME_VALIDATION_REGEX)) {
			throw new SiteSettingsValidationException(SiteSettingsValidationExceptionCause.INVALID_NAME);
		}

		Path imagePath = Paths.get(logoImagePath);

		if (!imagePath.getFileName().toString().matches(LOGO_IMAGE_VALIDATION_REGEX)) {
			throw new SiteSettingsValidationException(SiteSettingsValidationExceptionCause.INVALID_LOGO);
		}

		try {
			return new SiteSettingsBuilder().setDeliveryUrl(deliveryUrl).setName(name).setLogo(imagePath).build();
		} catch (SiteSettingsBuilderException e) {
			Logger logger = Logger.getLogger(SiteSettingsUseCase.class);
			logger.warn(e.getMessage(), e);
			throw new SiteSettingsValidationException(SiteSettingsValidationExceptionCause.INVALID_LOGO);
		}
	}

	public SiteSettings validateSiteSettings(String deliveryUrl, String name, byte[] logo)
			throws SiteSettingsValidationException {

		if (!deliveryUrl.matches(DELIVERY_URL_VALIDATION_REGEX)) {
			throw new SiteSettingsValidationException(SiteSettingsValidationExceptionCause.INVALID_DELIVERY_URL);
		}

		if (!name.matches(NAME_VALIDATION_REGEX)) {
			throw new SiteSettingsValidationException(SiteSettingsValidationExceptionCause.INVALID_NAME);
		}

		return new SiteSettingsBuilder().setDeliveryUrl(deliveryUrl).setName(name).setLogo(logo).build();
	}

	public void saveSiteSettings(SiteSettings siteSettings) {
		siteSettingsRepository.save(CopyUtil.createAndCopyFields(SiteSettingsEntity.class, siteSettings));
	}

	public void deleteSiteSettings(String deliveryUrl) {
		siteSettingsRepository.delete(siteSettingsRepository.findByDeliveryUrl(deliveryUrl));
	}

	public SiteSettings getSiteSettings() {
		Iterable<SiteSettingsEntity> siteSettings = siteSettingsRepository.findAll();

		if (Objects.isNull(siteSettings) || !siteSettings.iterator().hasNext()) {
			return null;
		}
		
		return CopyUtil.createAndCopyFields(SiteSettings.class, siteSettings.iterator().next());
	}
}
