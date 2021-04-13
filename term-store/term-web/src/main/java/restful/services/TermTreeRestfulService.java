package restful.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.ServletActionFactory;
import actions.TermTreeRestfulServcieActionFactory;

@WebServlet(urlPatterns = "/restful/terms")
public class TermTreeRestfulService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletActionFactory actionFactory = new TermTreeRestfulServcieActionFactory();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionFactory.getAction(req).doAction(req, resp);
	}
}
