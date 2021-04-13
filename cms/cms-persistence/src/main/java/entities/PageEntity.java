package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "pages")
public class PageEntity implements Serializable {
	@Id
	@Column(name = "PAGE_URI")
	private String uri;

	@Column(name = "PAGE_TITLE")
	private String title;

	@Column(name = "IS_HTML")
	private Boolean isHtml;

	@Column(name = "PAGE_SEO")
	private String seo;

	@Column(name = "PAGE_CONTENT")
	private String content;

	@Column(name = "PAGE_IS_PUBLISHED")
	private Boolean isPublished;

	@OneToOne
	@JoinColumn(name = "PAGE_SITE")
	private SiteEntity site;

	public PageEntity() {
		/* default constructor */
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

	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}
}
