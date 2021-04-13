package beans.builders;

import adapters.BeanBuilder;
import beans.User;

/**
 * Created by u624 on 3/24/17.
 */
public class UserBuilder implements BeanBuilder<User> {
    private User user = new User();

    public UserBuilder setUsername(String username) {
        user.setUsername(username);
        return this;
    }

    public UserBuilder setFullName(String fullName) {
        user.setFullName(fullName);
        return this;
    }

    public UserBuilder setPasswordHashCode(String passwordHashCode) {
        user.setPasswordHashCode(passwordHashCode);
        return this;
    }

    @Override
    public User build() {
        return user;
    }
}
