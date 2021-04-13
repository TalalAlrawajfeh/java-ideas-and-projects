package beans.builders;

import beans.Page;
import beans.Site;

public class SiteBuilder implements BeanBuilder<Site> {
	private Site site = new Site();

	public SiteBuilder setUri(String uri) {
		site.setUri(uri);
		return this;
	}

	public SiteBuilder setName(String name) {
		site.setName(name);
		return this;
	}

	public SiteBuilder setParentSite(Site parentSite) {
		site.setParentSite(parentSite);
		return this;
	}

	public SiteBuilder setLandingPage(Page landingPage) {
		site.setLandingPage(landingPage);
		return this;
	}

	@Override
	public Site build() {
		return site;
	}
}
