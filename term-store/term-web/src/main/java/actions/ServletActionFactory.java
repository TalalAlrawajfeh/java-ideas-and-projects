package actions;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface ServletActionFactory {
	ServletAction getAction(HttpServletRequest req);
}
