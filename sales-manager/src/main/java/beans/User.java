package beans;

import adapters.Convertable;
import entities.UserEntity;
import entities.builders.UserEntityBuilder;

import java.io.Serializable;

/**
 * Created by u624 on 3/24/17.
 */
public class User implements Serializable, Convertable<UserEntity> {
    private String username;
    private String fullName;
    private String passwordHashCode;

    public User() {
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
    public UserEntity convert() {
        return new UserEntityBuilder()
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
        User user = (User) o;
        if (username != null ? !username.equals(user.username) : user.username != null) {
            return false;
        }
        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) {
            return false;
        }
        return passwordHashCode != null ? passwordHashCode.equals(user.passwordHashCode) : user.passwordHashCode == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (passwordHashCode != null ? passwordHashCode.hashCode() : 0);
        return result;
    }
}
