package usecases;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import beans.User;
import persistence.UserRepository;
import utils.CopyUtil;

public class UserManagementUseCase {
	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), true)
				.map(u -> CopyUtil.createAndCopyFields(User.class, u)).collect(Collectors.toList());
	}
}
