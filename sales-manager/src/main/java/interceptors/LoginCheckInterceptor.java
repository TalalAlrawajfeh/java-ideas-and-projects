package interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by u624 on 3/22/17.
 */
public class LoginCheckInterceptor implements HandlerInterceptor {
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_PAGE_URL = "/login";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (Objects.isNull(httpServletRequest.getSession().getAttribute(USER_SESSION_ATTRIBUTE_NAME))) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LOGIN_PAGE_URL);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        /* not needed */
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        /* not needed */
    }
}
