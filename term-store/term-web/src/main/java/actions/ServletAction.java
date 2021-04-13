package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface ServletAction {
	void doAction(HttpServletRequest req, HttpServletResponse resp);
}
