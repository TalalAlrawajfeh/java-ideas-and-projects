package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
	@Id
	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD_HASHCODE")
	private String passwordHashCode;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "IS_ENABLED")
	private Boolean enabled;

	public UserEntity() {
		/* default constructor */
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHashCode() {
		return passwordHashCode;
	}

	public void setPasswordHashCode(String passwordHashCode) {
		this.passwordHashCode = passwordHashCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
