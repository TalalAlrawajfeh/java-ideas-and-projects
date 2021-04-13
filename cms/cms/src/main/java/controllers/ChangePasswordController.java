package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import beans.User;
import usecases.EditUserUseCase;
import utils.CopyUtil;
import utils.HashUtil;

@Controller
public class ChangePasswordController {
	private static final String REDIRECT_USER_MANAGEMENT = "redirect:/user-management";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String INCORRECT_PASS_MESSAGE = "The old password is incorrect";
	private static final String USER_SESSION_ATTRIBUTE = "user";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String CHANGE_PASS_JSP = "ChangePassword";
	private static final String CHANGE_PASS_URL = "/change-password";
	private static final String CANCEL_ACTION = "cancel";
	private static final String BASE_JSP = "Base";

	@Autowired
	private EditUserUseCase editUserUseCase;

	@RequestMapping(value = CHANGE_PASS_URL, method = RequestMethod.GET)
	public ModelAndView changePassword(HttpServletRequest req, HttpServletResponse resp) {
		setProperAttribtutes(req, false);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = CHANGE_PASS_URL, method = RequestMethod.POST)
	public ModelAndView changePassword(HttpServletRequest req, HttpServletResponse resp, @RequestParam String action,
			@RequestParam String oldPassword, @RequestParam String newPassword) {

		if (CANCEL_ACTION.equals(action)) {
			return new ModelAndView(REDIRECT_USER_MANAGEMENT);
		}

		User oldUser = (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE);

		if (!oldUser.getPasswordHashCode().equals(HashUtil.hashString(oldPassword))) {
			setProperAttribtutes(req, true);
			return new ModelAndView(BASE_JSP);
		}

		updateUser(req, oldUser, newPassword);
		return new ModelAndView(REDIRECT_USER_MANAGEMENT);
	}

	private void updateUser(HttpServletRequest req, User user, String password) {
		User newUser = CopyUtil.createAndCopyFields(User.class, user);
		newUser.setHashedPassword(password);
		editUserUseCase.updateUser(user, newUser);
		req.getSession().setAttribute(USER_SESSION_ATTRIBUTE, newUser);
	}

	private void setProperAttribtutes(HttpServletRequest req, boolean showErrorMessage) {
		req.setAttribute(SHOW_ERROR_ATTRIBUTE, showErrorMessage);
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, CHANGE_PASS_JSP);

		if (showErrorMessage) {
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, INCORRECT_PASS_MESSAGE);
		}
	}
}
