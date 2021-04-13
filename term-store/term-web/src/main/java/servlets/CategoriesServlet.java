package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.CategoriesActionFactory;

@WebServlet(urlPatterns = "/categories")
public class CategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoriesActionFactory actionFactory = new CategoriesActionFactory();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionFactory.getAction("gotoTermCategoryListJsp").doAction(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionFactory.getAction(req).doAction(req, resp);
	}
}
