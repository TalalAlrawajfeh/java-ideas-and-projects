package usecases;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.Site;
import beans.builders.SiteBuilder;
import entities.SiteEntity;
import persistence.SiteRepository;
import usecases.exceptions.SiteValidationException;
import usecases.exceptions.SiteValidationException.SiteValidationExceptionCause;
import utils.EntityCopyUtil;

public class AddSiteUseCase {
	private static final String SITE_NAME_VALIDATION_REGEX = "[a-zA-Z0-9\\s]{3,50}";
	private static final String SITE_URI_VALIDATION_REGEX = "/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	@Autowired
	private SiteRepository siteRepository;

	public void saveSite(Site site) {
		siteRepository.save(EntityCopyUtil.createAndCopyFields(SiteEntity.class, site));
	}

	public boolean siteExists(String uri) {
		return Objects.nonNull(siteRepository.findByUri(uri));
	}

	public Site validateSite(String name, String uri) throws SiteValidationException {
		if (!name.matches(SITE_NAME_VALIDATION_REGEX)) {
			throw new SiteValidationException(SiteValidationExceptionCause.INVALID_NAME);
		}

		if (!uri.matches(SITE_URI_VALIDATION_REGEX)) {
			throw new SiteValidationException(SiteValidationExceptionCause.INVALID_URI);
		}

		return new SiteBuilder().setName(name).setUri(uri).build();
	}
}
