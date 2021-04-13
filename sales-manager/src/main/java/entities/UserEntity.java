package entities;

import adapters.Convertable;
import beans.User;
import beans.builders.UserBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by u624 on 3/24/17.
 */
@Entity
@Table(name = "users")
public class UserEntity implements Serializable, Convertable<User> {
    @Id
    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "PASSWORD")
    private String passwordHashCode;

    public UserEntity() {
        /* default constructor */
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordHashCode() {
        return passwordHashCode;
    }

    public void setPasswordHashCode(String passwordHashCode) {
        this.passwordHashCode = passwordHashCode;
    }

    @Override
    public User convert() {
        return new UserBuilder()
                .setUsername(username)
                .setFullName(fullName)
                .setPasswordHashCode(passwordHashCode)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        if (username != null ? !username.equals(that.username) : that.username != null) {
            return false;
        }
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) {
            return false;
        }
        return passwordHashCode != null ? passwordHashCode.equals(that.passwordHashCode) : that.passwordHashCode == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (passwordHashCode != null ? passwordHashCode.hashCode() : 0);
        return result;
    }
}
