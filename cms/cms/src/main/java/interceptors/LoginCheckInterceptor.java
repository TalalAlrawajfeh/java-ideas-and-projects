
package interceptors;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import beans.User;

public class LoginCheckInterceptor implements HandlerInterceptor {
	private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
	private static final String USER_SESSION_ATTRIBUTE = "user";
	private static final String CURRENT_USER_ATTRIBUTE = "currentUser";
	private static final String LOGIN_URL = "/login";

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception ex)
			throws Exception {
		/* Nothing needed to be implemented here for now */
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView modelAndView)
			throws Exception {
		/* Nothing needed to be implemented here for now */
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		if (Objects.isNull((User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE_NAME))) {
			resp.sendRedirect(req.getContextPath() + LOGIN_URL);
			return false;
		}

		req.setAttribute(CURRENT_USER_ATTRIBUTE, (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE));
		return true;
	}
}
