package initializers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.User;
import beans.builders.UserBuilder;
import entities.UserEntity;
import persistence.UserRepository;
import utils.CopyUtil;

public class FirstTimeAdminCreator implements Initializer {
	private static final String ADMIN_USERNAME = "admin";
	private static final String COMPLEX = "P@ssw0rd";

	@Autowired
	private UserRepository userRepository;

	@Override
	public void initialize() {
		if (Objects.isNull(userRepository.findByUsername(ADMIN_USERNAME))) {
			userRepository.save(CopyUtil.createAndCopyFields(UserEntity.class, buildAdmin()));
		}
	}

	private User buildAdmin() {
		return new UserBuilder().setEnabled(true).setFullName(ADMIN_USERNAME).setUsername(ADMIN_USERNAME)
				.setHashedPassword(COMPLEX).build();
	}
}
