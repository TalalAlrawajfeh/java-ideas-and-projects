package usecases;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import beans.Page;
import persistence.PageRepository;
import utils.EntityCopyUtil;

public class PageManagementUseCase {
	@Autowired
	private PageRepository pageRepository;

	public List<Page> getAllPages() {
		return StreamSupport.stream(pageRepository.findAll().spliterator(), true)
				.map(p -> EntityCopyUtil.createAndCopyFields(Page.class, p)).collect(Collectors.toList());
	}

	public Page getPageByUri(String uri) {
		return EntityCopyUtil.createAndCopyFields(Page.class, pageRepository.findByUri(uri));
	}

	public List<Page> getPagesOfSite(String uri) {
		return StreamSupport.stream(getAllPages().spliterator(), true).filter(p -> p.getSite().getUri().equals(uri))
				.collect(Collectors.toList());
	}
}
