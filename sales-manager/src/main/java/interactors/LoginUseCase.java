package interactors;

import adapters.UseCase;
import beans.Pair;
import beans.User;
import entities.UserEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by u624 on 3/24/17.
 */
public class LoginUseCase implements UseCase<User> {
    @Autowired
    private UserRepository userRepository;

    private List<Pair<Predicate<User>, String>> validations = new ArrayList<>();

    public LoginUseCase() {
        validations.add(new Pair<>(u -> Objects.nonNull(u),
                "User cannot be null"));
        validations.add(new Pair<>(u -> Objects.nonNull(u.getUsername()),
                "Username cannot be null"));
        validations.add(new Pair<>(u -> Objects.nonNull(u.getPasswordHashCode()),
                "Password cannot be null"));
    }

    @Override
    public void execute(User user) throws UseCaseException {
        validateUser(user);
        validateUser(user, userRepository.findByUsername(user.getUsername()));
    }

    private void validateUser(User user, UserEntity userEntity) throws UseCaseException {
        if (Objects.isNull(userEntity) || !userEntity.getPasswordHashCode().equals(user.getPasswordHashCode())) {
            throw new UseCaseException("Invalid username or password");
        }
    }

    private void validateUser(User user) throws UseCaseException {
        Optional<Pair<Predicate<User>, String>> validationPair = validations
                .stream()
                .sequential()
                .filter(e -> !e.getFirst().test(user))
                .findAny();
        if (validationPair.isPresent()) {
            throw new UseCaseException(validationPair.get().getSecond());
        }
    }
}
