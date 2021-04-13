package controllers;

import java.util.EnumMap;
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
import usecases.exceptions.UserValidationException;
import usecases.exceptions.UserValidationException.UserValidationExceptionCause;

@Controller
public class AddUserController {
	private static final String INVALID_USERNAME_EXCEPTION_MESSGAGE = "Invalid username";
	private static final String INVALID_FULLNAME_EXCEPTION_MESSAGE = "Invalid full name";
	private static final String DUPLICATE_USERNAME_ERROR_MESSAGE = "You entered a username that already exist";
	private static final String REDIRECT_USER_MANAGEMENT = "redirect:/user-management";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String OTHER_EXCEPTION_MESSAGE = "An error occured";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String ADD_USER_JSP = "AddUser";
	private static final String ADD_USER_URL = "/add-user";
	private static final String BASE_JSP = "Base";
	private static final String COMPLEX = "P@ssw0rd";

	private EnumMap<UserValidationExceptionCause, String> errorMessageMap = new EnumMap<>(
			UserValidationExceptionCause.class);

	@Autowired
	private AddUserUseCase addUserUseCase;

	public AddUserController() {
		errorMessageMap.put(UserValidationExceptionCause.INVALID_FULLNAME, INVALID_FULLNAME_EXCEPTION_MESSAGE);
		errorMessageMap.put(UserValidationExceptionCause.INVALID_USERNAME, INVALID_USERNAME_EXCEPTION_MESSGAGE);
		errorMessageMap.put(UserValidationExceptionCause.OTHER, OTHER_EXCEPTION_MESSAGE);
	}

	@RequestMapping(value = ADD_USER_URL, method = RequestMethod.GET)
	public ModelAndView addUser(HttpServletRequest req, HttpServletResponse resp) {
		setProperAttribtutes(req, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = ADD_USER_URL, method = RequestMethod.POST)
	public ModelAndView addUser(HttpServletRequest req, HttpServletResponse resp, @RequestParam String fullName,
			@RequestParam String username) {

		try {
			User user = addUserUseCase.validateUser(fullName, username);
			user.setEnabled(true);
			user.setHashedPassword(COMPLEX);

			if (addUserUseCase.userExists(username)) {
				setProperAttribtutes(req, DUPLICATE_USERNAME_ERROR_MESSAGE);
				return new ModelAndView(BASE_JSP);
			}

			addUserUseCase.saveUser(user);
		} catch (UserValidationException e) {
			String errorMessage = errorMessageMap.get(e.getValidationExceptionCause());
			Logger logger = Logger.getLogger(AddUserController.class);
			logger.info(errorMessage, e);

			setProperAttribtutes(req, errorMessage);
			return new ModelAndView(BASE_JSP);
		}

		return new ModelAndView(REDIRECT_USER_MANAGEMENT);
	}

	private void setProperAttribtutes(HttpServletRequest req, String errorMessage) {
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, ADD_USER_JSP);

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}
}
