package usecases;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.Page;
import entities.PageEntity;
import entities.PublishedPageEntity;
import entities.SiteEntity;
import persistence.PageRepository;
import persistence.PublishedPageRepository;
import utils.CopySetting;
import utils.EntityCopyUtil;

public class EditPageUseCase {
	private static final CopySetting[] COPY_SETTINGS = new CopySetting[] {
			new CopySetting(SiteEntity.class, SiteEntity.class), new CopySetting(PageEntity.class, PageEntity.class) };

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private PublishedPageRepository publishedPageRepository;

	public void deletePageByUri(String uri) {
		pageRepository.delete(pageRepository.findByUri(uri));
	}

	public void publishPage(String uri) {
		PageEntity pageEntity = pageRepository.findByUri(uri);
		PublishedPageEntity correspondingPublishedPage = publishedPageRepository.findByCorrespondingPage(pageEntity);

		if (Objects.nonNull(correspondingPublishedPage)) {
			publishedPageRepository.delete(correspondingPublishedPage);
		}

		PublishedPageEntity publishedPage = EntityCopyUtil.createAndCopyFields(PublishedPageEntity.class, pageEntity,
				COPY_SETTINGS);

		publishedPage.setCorrespondingPage(pageEntity);
		publishedPageRepository.save(publishedPage);

		pageEntity.setIsPublished(Boolean.valueOf(true));
		pageRepository.save(pageEntity);
	}

	public boolean wasPublished(String uri) {
		return Objects.nonNull(getCorrespondingPublishedPage(uri));
	}

	public Page getCorrespondingPublishedPage(Page page) {
		return EntityCopyUtil.createAndCopyFields(Page.class, getCorrespondingPublishedPage(page.getUri()));
	}

	public void deleteCorrespondingPublishedPage(String uri) {
		publishedPageRepository.delete(getCorrespondingPublishedPage(uri));
	}

	private PublishedPageEntity getCorrespondingPublishedPage(String uri) {
		return publishedPageRepository.findByCorrespondingPage(pageRepository.findByUri(uri));
	}
}
