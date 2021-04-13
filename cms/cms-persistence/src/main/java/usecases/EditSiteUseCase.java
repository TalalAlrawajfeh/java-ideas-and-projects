package usecases;

import org.springframework.beans.factory.annotation.Autowired;

import beans.Site;
import entities.SiteEntity;
import persistence.SiteRepository;
import utils.EntityCopyUtil;

public class EditSiteUseCase {
	@Autowired
	private SiteRepository siteRepository;

	public Site getSiteByUri(String uri) {
		return EntityCopyUtil.createAndCopyFields(Site.class, siteRepository.findByUri(uri));
	}

	public void updateSite(Site site) {
		siteRepository.save(EntityCopyUtil.createAndCopyFields(SiteEntity.class, site));
	}
}
