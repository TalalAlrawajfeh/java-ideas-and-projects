
package usecases;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.User;
import entities.UserEntity;
import persistence.UserRepository;
import utils.CopyUtil;

public class EditUserUseCase {
	@Autowired
	private UserRepository userRepository;

	public User getUserFromUsername(String username) {
		UserEntity userEntity = userRepository.findByUsername(username);

		if (Objects.isNull(userEntity)) {
			return null;
		}

		return CopyUtil.createAndCopyFields(User.class, userEntity);
	}

	public void updateUser(User oldUser, User newUser) {
		userRepository.delete(CopyUtil.createAndCopyFields(UserEntity.class, oldUser));
		userRepository.save(CopyUtil.createAndCopyFields(UserEntity.class, newUser));
	}
}
