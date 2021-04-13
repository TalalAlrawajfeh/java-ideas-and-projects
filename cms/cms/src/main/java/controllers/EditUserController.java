package controllers;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import usecases.AddUserUseCase;
import usecases.EditUserUseCase;
import usecases.exceptions.UserValidationException;
import usecases.exceptions.UserValidationException.UserValidationExceptionCause;
import utils.CopyUtil;

@Controller
public class EditUserController {
	private static final String REDIRECT_EDIT_USER_WITH_USERNAME_PARAMETER = "redirect:/edit-user?username=";
	private static final String INVALID_USERNAME_EXCEPTION_MESSGAGE = "Invalid username";
	private static final String INVALID_FULLNAME_EXCEPTION_MESSAGE = "Invalid full name";
	private static final String REDIRECT_USER_MANAGEMENT = "redirect:/user-management";
	private static final String OTHER_EXCEPTION_MESSAGE = "An error occured";
	private static final String MANAGED_USER_ATTRIBUTE = "managedUser";
	private static final String USER_SESSION_ATTRIBUTE = "user";
	private static final String DUPLICATE_USER_MESSAGE = "A user with the same username already exists.";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String INCLUDED_PAGE_JSP = "includedPage";
	private static final String DISABLE_ACTION = "disable";
	private static final String ADMIN_USERNAME = "admin";
	private static final String ENABLE_ACTION = "enable";
	private static final String CANCEL_ACTION = "cancel";
	private static final String ERROR_MESSAGE = "errorMessage";
	private static final String EDIT_USER_JSP = "EditUser";
	private static final String EDIT_USER_URL = "/edit-user";
	private static final String RESET_ACTION = "reset";
	private static final String SAVE_ACTION = "save";
	private static final String BASE_JSP = "Base";
	private static final String COMPLEX = "P@ssw0rd";

	@Autowired
	private EditUserUseCase editUserUserCase;

	@Autowired
	private AddUserUseCase addUserUseCase;

	private Map<String, TriConsumer<String, String, User>> actionMap = new HashMap<>();
	private EnumMap<UserValidationExceptionCause, String> errorMessageMap = new EnumMap<>(
			UserValidationExceptionCause.class);

	public EditUserController() {
		actionMap.put(SAVE_ACTION, (username, fullName, user) -> {
			user.setUsername(username);
			user.setFullName(fullName);
		});

		actionMap.put(DISABLE_ACTION, (username, fullName, user) -> user.setEnabled(false));
		actionMap.put(ENABLE_ACTION, (username, fullName, user) -> user.setEnabled(true));
		actionMap.put(RESET_ACTION, (username, fullName, user) -> user.setHashedPassword(COMPLEX));

		errorMessageMap.put(UserValidationExceptionCause.INVALID_FULLNAME, INVALID_FULLNAME_EXCEPTION_MESSAGE);
		errorMessageMap.put(UserValidationExceptionCause.INVALID_USERNAME, INVALID_USERNAME_EXCEPTION_MESSGAGE);
		errorMessageMap.put(UserValidationExceptionCause.OTHER, OTHER_EXCEPTION_MESSAGE);
	}

	@RequestMapping(value = EDIT_USER_URL, method = RequestMethod.GET)
	public ModelAndView editUser(HttpServletRequest req, HttpServletResponse resp, @RequestParam String username) {
		if (username.equals(ADMIN_USERNAME)) {
			return new ModelAndView(REDIRECT_USER_MANAGEMENT);
		}

		setProperAttribtutes(req, username, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = EDIT_USER_URL, method = RequestMethod.POST)
	public ModelAndView editUser(HttpServletRequest req, HttpServletResponse resp, @RequestParam String action,
			@RequestParam String managedUsername, @RequestParam String fullName, @RequestParam String username) {

		if (action.equals(CANCEL_ACTION)) {
			return new ModelAndView(REDIRECT_USER_MANAGEMENT);
		}

		try {
			addUserUseCase.validateUser(fullName, username);
		} catch (UserValidationException e) {
			String errorMessage = errorMessageMap.get(e.getValidationExceptionCause());
			Logger logger = Logger.getLogger(EditUserController.class);
			logger.info(errorMessage, e);

			setProperAttribtutes(req, managedUsername, errorMessage);
			return new ModelAndView(BASE_JSP);
		}

		User oldUser = editUserUserCase.getUserFromUsername(managedUsername);
		User foundUser = editUserUserCase.getUserFromUsername(username);

		if (Objects.nonNull(foundUser) && !oldUser.equals(foundUser)) {
			setProperAttribtutes(req, managedUsername, DUPLICATE_USER_MESSAGE);
			return new ModelAndView(BASE_JSP);
		}

		User newUser = updateUser(action, fullName, username, oldUser);
		resetSessionIfCurrentUserChanged(req, managedUsername, newUser);
		return new ModelAndView(REDIRECT_EDIT_USER_WITH_USERNAME_PARAMETER + newUser.getUsername());
	}

	private User updateUser(String action, String fullName, String username, User oldUser) {
		User newUser = CopyUtil.createAndCopyFields(User.class, oldUser);
		actionMap.get(action).apply(username, fullName, newUser);
		editUserUserCase.updateUser(oldUser, newUser);
		return newUser;
	}

	private void resetSessionIfCurrentUserChanged(HttpServletRequest req, String managedUsername, User newUser) {
		User user = (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE);

		if (managedUsername.equals(user.getUsername())) {
			req.getSession().setAttribute(USER_SESSION_ATTRIBUTE, newUser);
		}
	}

	private void setProperAttribtutes(HttpServletRequest req, String username, String errorMessage) {
		req.setAttribute(MANAGED_USER_ATTRIBUTE, editUserUserCase.getUserFromUsername(username));
		req.setAttribute(INCLUDED_PAGE_JSP, EDIT_USER_JSP);

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}

	@FunctionalInterface
	private interface TriConsumer<T1, T2, T3> {
		public void apply(T1 t1, T2 t2, T3 t3);
	}
}
