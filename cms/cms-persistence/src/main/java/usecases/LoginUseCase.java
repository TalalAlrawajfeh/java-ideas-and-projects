package usecases;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import beans.User;
import entities.UserEntity;
import persistence.UserRepository;
import usecases.exceptions.InvalidUserException;
import usecases.exceptions.InvalidUserException.InvalidUserExceptionCause;
import utils.CopyUtil;
import utils.HashUtil;

public class LoginUseCase {
	@Autowired
	private UserRepository userRepository;

	public User validateUser(String username, String password) throws InvalidUserException {
		UserEntity userEntity = userRepository.findByUsername(username);
		if (Objects.nonNull(userEntity)) {
			if (userEntity.getPasswordHashCode().equalsIgnoreCase(HashUtil.hashString(password))) {
				return CopyUtil.createAndCopyFields(User.class, userEntity);
			} else {
				throw new InvalidUserException(InvalidUserExceptionCause.INVALID_PASSWORD);
			}
		} else {
			throw new InvalidUserException(InvalidUserExceptionCause.USER_NOT_FOUND);
		}
	}
}
