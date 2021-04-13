package usecases;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.Page;
import entities.PageEntity;
import persistence.PageRepository;
import utils.EntityCopyUtil;

public class AddPageUseCase {
	private static final String PAGE_URI_VALIDATION_REGEX = "/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	@Autowired
	private PageRepository pageRepository;

	public void savePage(Page page) {
		pageRepository.save(EntityCopyUtil.createAndCopyFields(PageEntity.class, page));
	}

	public boolean pageExists(String uri) {
		return Objects.nonNull(pageRepository.findByUri(uri));
	}

	public boolean isPageUriValid(String uri) {
		return uri.matches(PAGE_URI_VALIDATION_REGEX);
	}
}
