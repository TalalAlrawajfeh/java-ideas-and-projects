package entities.builders;

import adapters.BeanBuilder;
import entities.UserEntity;

/**
 * Created by u624 on 3/24/17.
 */
public class UserEntityBuilder implements BeanBuilder<UserEntity> {
    private UserEntity userEntity = new UserEntity();

    public UserEntityBuilder setUsername(String username) {
        userEntity.setUsername(username);
        return this;
    }

    public UserEntityBuilder setFullName(String fullName) {
        userEntity.setFullName(fullName);
        return this;
    }

    public UserEntityBuilder setPasswordHashCode(String passwordHashCode) {
        userEntity.setPasswordHashCode(passwordHashCode);
        return this;
    }

    @Override
    public UserEntity build() {
        return userEntity;
    }
}
