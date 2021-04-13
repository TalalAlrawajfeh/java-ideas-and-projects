package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Site implements Serializable {
	private String uri;
	private String name;
	private Site parentSite;
	private Page landingPage;

	public Site() {
		/* default constructor */
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Site getParentSite() {
		return parentSite;
	}

	public void setParentSite(Site parentSite) {
		this.parentSite = parentSite;
	}

	public Page getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(Page landingPage) {
		this.landingPage = landingPage;
	}
}
