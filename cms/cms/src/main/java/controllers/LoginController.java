package controllers;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import beans.User;
import usecases.LoginUseCase;
import usecases.exceptions.InvalidUserException;
import usecases.exceptions.InvalidUserException.InvalidUserExceptionCause;

@Controller
public class LoginController {
	private static final String ATTEMPT_TO_LOGIN_WITH_UNKNOWN_USER_LOG = "An attempt to login with unknown user ";
	private static final String FAILED_TO_LOGIN_INCORRECT_PASS_LOG = " has failed to log in due to incorrect password";
	private static final String INCORRECT_USERNAME_OR_PASS_MESSAGE = "Incorrect username or password";
	private static final String USER_RETURNED_TO_LOGIN_PAGE_LOG = " has returned to login page and will be redirected to user management page";
	private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
	private static final String USER_HAS_LOGGED_IN_LOG = " has logged in";
	private static final String DISABLED_USER_MESSAGE = "The user is disabled";
	private static final String USER_MANAGEMENT_PAGE = "redirect:/user-management";
	private static final String LOGIN_CONTROLLER_URL = "/login";
	private static final String ERROR_MESSAGE = "errorMessage";
	private static final String USER_STRING = "User ";
	private static final String SHOW_ERROR = "showError";
	private static final String LOGIN_PAGE = "Login";

	private static Logger logger = Logger.getLogger(LoginController.class);
	
	private EnumMap<InvalidUserExceptionCause, Consumer<String>> causeMap = new EnumMap<>(
			InvalidUserExceptionCause.class);

	@Autowired
	private LoginUseCase loginUseCase;

	public LoginController() {
		causeMap.put(InvalidUserExceptionCause.INVALID_PASSWORD,
				username -> logger.warn(USER_STRING + username + FAILED_TO_LOGIN_INCORRECT_PASS_LOG));

		causeMap.put(InvalidUserExceptionCause.USER_NOT_FOUND,
				username -> logger.info(ATTEMPT_TO_LOGIN_WITH_UNKNOWN_USER_LOG + username));
	}

	@RequestMapping(value = LOGIN_CONTROLLER_URL, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) {
		req.setAttribute(SHOW_ERROR, false);
		User user = (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE_NAME);

		if (Objects.nonNull(user)) {
			logger.info(USER_STRING + user.getUsername() + USER_RETURNED_TO_LOGIN_PAGE_LOG);
			return new ModelAndView(USER_MANAGEMENT_PAGE);
		}

		return new ModelAndView(LOGIN_PAGE);
	}

	@RequestMapping(value = LOGIN_CONTROLLER_URL, method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest req, HttpServletResponse resp, @RequestParam String username,
			@RequestParam String password) {

		try {
			User user = loginUseCase.validateUser(username, password);

			if (user.getEnabled()) {
				logger.info(USER_STRING + username + USER_HAS_LOGGED_IN_LOG);
				req.getSession(true).setAttribute(USER_SESSION_ATTRIBUTE_NAME, user);
				return new ModelAndView(USER_MANAGEMENT_PAGE);
			}

			req.setAttribute(ERROR_MESSAGE, DISABLED_USER_MESSAGE);
		} catch (InvalidUserException e) {
			logger.error(e);
			causeMap.get(e.getInvalidUserCause()).accept(username);
			req.setAttribute(ERROR_MESSAGE, INCORRECT_USERNAME_OR_PASS_MESSAGE);
		}

		req.setAttribute(SHOW_ERROR, true);
		return new ModelAndView(LOGIN_PAGE);
	}
}
