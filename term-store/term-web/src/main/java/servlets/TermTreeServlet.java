package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.TermTreeActionFactory;

@WebServlet(urlPatterns = "/term-tree")
public class TermTreeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	TermTreeActionFactory actionFactory = new TermTreeActionFactory();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionFactory.getGETRestfulAction(req).doAction(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionFactory.getAction(req).doAction(req, resp);
	}
}
