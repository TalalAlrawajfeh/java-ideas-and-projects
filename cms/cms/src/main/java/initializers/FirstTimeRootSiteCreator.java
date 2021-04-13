package initializers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.builders.PageBuilder;
import beans.builders.SiteBuilder;
import entities.PageEntity;
import entities.SiteEntity;
import persistence.PageRepository;
import persistence.SiteRepository;
import utils.EntityCopyUtil;

public class FirstTimeRootSiteCreator implements Initializer {
	private static final String WELCOME_TITLE = "Welcome";
	private static final String WELCOME_URI = "/root/welcome";
	private static final String ROOT_NAME = "Root";
	private static final String ROOT_URI = "/root";

	@Autowired
	private SiteRepository siteRepository;

	@Autowired
	private PageRepository pageRepository;

	@Override
	public void initialize() {
		if (Objects.isNull(siteRepository.findByUri(ROOT_URI))) {
			SiteEntity root = EntityCopyUtil.createAndCopyFields(SiteEntity.class,
					new SiteBuilder().setName(ROOT_NAME).setUri(ROOT_URI).build());
			siteRepository.save(root);

			PageEntity welcome = EntityCopyUtil.createAndCopyFields(PageEntity.class,
					new PageBuilder().setTitle(WELCOME_TITLE).setUri(WELCOME_URI).setIsPublished(false).build());
			pageRepository.save(welcome);

			root.setLandingPage(welcome);
			welcome.setSite(root);
			siteRepository.save(root);
			pageRepository.save(welcome);
		}
	}
}
