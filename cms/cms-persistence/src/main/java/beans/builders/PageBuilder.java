package beans.builders;

import beans.Page;
import beans.Site;

public class PageBuilder implements BeanBuilder<Page> {
	private Page page = new Page();

	public PageBuilder setUri(String uri) {
		page.setUri(uri);
		return this;
	}

	public PageBuilder setTitle(String title) {
		page.setTitle(title);
		return this;
	}

	public PageBuilder setIsHtml(Boolean isHtml) {
		page.setIsHtml(isHtml);
		return this;
	}

	public PageBuilder setContent(String content) {
		page.setContent(content);
		return this;
	}

	public PageBuilder setSeo(String seo) {
		page.setSeo(seo);
		return this;
	}

	public PageBuilder setSite(Site site) {
		page.setSite(site);
		return this;
	}

	public PageBuilder setIsPublished(Boolean isPublished) {
		page.setIsPublished(isPublished);
		return this;
	}

	@Override
	public Page build() {
		return page;
	}
}
