package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author u624
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "published_pages")
public class PublishedPageEntity implements Serializable {
	@Id
	@Column(name = "PUBLISHED_PAGE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "PUBLISHED_PAGE_URI")
	private String uri;

	@Column(name = "PUBLISHED_PAGE_TITLE")
	private String title;

	@Column(name = "PUBLISHED_IS_HTML")
	private Boolean isHtml;

	@Column(name = "PUBLISHED_PAGE_SEO")
	private String seo;

	@Column(name = "PUBLISHED_PAGE_CONTENT")
	private String content;

	@OneToOne
	@JoinColumn(name = "PUBLISHED_PAGE_SITE")
	private SiteEntity site;

	@OneToOne
	@JoinColumn(name = "CORRESPONDING_PAGE")
	private PageEntity correspondingPage;

	public PublishedPageEntity() {
		/* default constructor */
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Boolean isHtml) {
		this.isHtml = isHtml;
	}

	public String getSeo() {
		return seo;
	}

	public void setSeo(String seo) {
		this.seo = seo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SiteEntity getSite() {
		return site;
	}

	public void setSite(SiteEntity site) {
		this.site = site;
	}

	public PageEntity getCorrespondingPage() {
		return correspondingPage;
	}

	public void setCorrespondingPage(PageEntity correspondingPage) {
		this.correspondingPage = correspondingPage;
	}
}
