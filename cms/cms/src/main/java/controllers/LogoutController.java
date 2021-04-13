package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController {
	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String LOGOUT_URL = "/logout";

	@RequestMapping(value = LOGOUT_URL, method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().invalidate();
		return new ModelAndView(REDIRECT_LOGIN);
	}
}
