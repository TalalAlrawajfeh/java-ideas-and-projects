package interactors;

import beans.builders.UserBuilder;
import entities.builders.UserEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.UserRepository;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUseCaseTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginUseCase loginUseCase = new LoginUseCase();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void GivenNullUserThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        expectUseCaseException("User cannot be null");
        loginUseCase.execute(null);
    }

    @Test
    public void GivenUserWithNullUserNameThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        expectUseCaseException("Username cannot be null");
        loginUseCase.execute(new UserBuilder().setUsername(null).setPasswordHashCode("123").build());
    }

    @Test
    public void GivenUserWithNullPasswordHashCodeThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        expectUseCaseException("Password cannot be null");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode(null).build());
    }

    @Test
    public void GivenUserWithThatDoesNotExistThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        expectUseCaseException("Invalid username or password");
        Mockito.doReturn(null).when(userRepository).findByUsername("user");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode("123").build());
    }

    @Test
    public void GivenUserWithPasswordHashCodeNotEqualToRealPasswordHashCodeThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        expectUseCaseException("Invalid username or password");
        Mockito.doReturn(new UserEntityBuilder().setPasswordHashCode("321").build()).when(userRepository).findByUsername("user");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode("123").build());
    }

    @Test
    public void GivenValidUserWithPasswordHashCodeEqualToRealPasswordHashCodeThenNoExceptionSouldBeThrown() throws UseCaseException {
        Mockito.doReturn(new UserEntityBuilder().setPasswordHashCode("123").build()).when(userRepository).findByUsername("user");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode("123").build());
    }

    private void expectUseCaseException(String expectedMessage) {
        expectedException.expect(UseCaseException.class);
        expectedException.expectMessage(expectedMessage);
    }
}
