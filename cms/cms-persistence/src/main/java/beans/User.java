package beans;

import java.io.Serializable;

import utils.HashUtil;

/**
 * @author u624
 */
@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String passwordHashCode;
	private String fullName;
	private Boolean enabled;

	public User() {
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

	public void setHashedPassword(String password) {
		passwordHashCode = HashUtil.hashString(password);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		return fullName.hashCode() ^ username.hashCode() ^ passwordHashCode.hashCode() ^ enabled.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User user = (User) obj;

			if (enabled.equals(user.getEnabled()) && username.equals(user.getUsername())
					&& fullName.equals(user.getFullName()) && passwordHashCode.equals(user.getPasswordHashCode())) {
				return true;
			}

			return false;
		}

		return super.equals(obj);
	}
}
