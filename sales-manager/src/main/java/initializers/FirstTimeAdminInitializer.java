package initializers;

import adapters.Initializer;
import entities.builders.UserEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.UserRepository;
import utilities.HashingUtility;

import java.util.Objects;

/**
 * Created by u624 on 3/24/17.
 */
public class FirstTimeAdminInitializer implements Initializer {
    private static final String ADMIN_FULL_NAME = "Anas Ahmad Alshahatit";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASS = "P@ssw0rd";

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init() {
        if (Objects.isNull(userRepository.findByUsername(ADMIN_USERNAME))) {
            userRepository.save(new UserEntityBuilder()
                    .setFullName(ADMIN_FULL_NAME)
                    .setUsername(ADMIN_USERNAME)
                    .setPasswordHashCode(HashingUtility.hashString(ADMIN_PASS))
                    .build());
        }
    }
}
