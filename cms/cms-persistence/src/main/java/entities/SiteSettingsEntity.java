package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author u624
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "website_settings")
public class SiteSettingsEntity implements Serializable {
	@Id
	@Column(name = "SITE_DELIVERY_URL")
	private String deliveryUrl;

	@Column(name = "SITE_NAME")
	private String name;

	@Lob
	@Column(name = "SITE_LOGO")
	private byte[] logo;

	public SiteSettingsEntity() {
		/* default constructor */
	}

	public String getDeliveryUrl() {
		return deliveryUrl;
	}

	public void setDeliveryUrl(String deliveryUrl) {
		this.deliveryUrl = deliveryUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
}
