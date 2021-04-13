package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author u624
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sites")
public class SiteEntity implements Serializable {
	@Id
	@Column(name = "SITE_URI")
	private String uri;

	@Column(name = "SITE_NAME")
	private String name;

	@ManyToOne
	@JoinColumn(name = "PARENT_SITE")
	private SiteEntity parentSite;

	@OneToOne
	@JoinColumn(name = "LANDING_PAGE")
	private PageEntity landingPage;

	@OneToMany(mappedBy = "parentSite", fetch = FetchType.EAGER)
	private List<SiteEntity> subSites;

	public SiteEntity() {
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

	public SiteEntity getParentSite() {
		return parentSite;
	}

	public void setParentSite(SiteEntity parentSite) {
		this.parentSite = parentSite;
	}

	public PageEntity getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(PageEntity landingPage) {
		this.landingPage = landingPage;
	}

	public List<SiteEntity> getSubSites() {
		return subSites;
	}

	public void setSubSites(List<SiteEntity> subSites) {
		this.subSites = subSites;
	}
}
