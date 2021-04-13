package servlets;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.CategoryPersistenceService;
import persistence.TermPersistenceService;
import persistence.beans.Category;
import persistence.beans.Term;

@WebServlet(urlPatterns = "/term-search")
public class TermSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CategoryPersistenceService categoryPersistenceService = new CategoryPersistenceService();
	TermPersistenceService TermPersistenceService = new TermPersistenceService();

	private Function<String, String> parameterStartsWith = s -> {
		if (Objects.nonNull(s)) {
			if (!"".equals(s.trim())) {
				return s.trim() + "%";
			} else {
				return null;
			}
		} else {
			return null;
		}
	};
	
	private Function<String, String> paramaterContains = s -> {
		if (Objects.nonNull(s)) {
			if (!"".equals(s.trim())) {
				return "%" + s.trim() + "%";
			} else {
				return null;
			}
		} else {
			return null;
		}
	};

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("categories",
				categoryPersistenceService.listAll().stream().map(c -> c.getName()).collect(Collectors.toList()));
		req.getRequestDispatcher("/WEB-INF/jsp/TermSearch.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Term term = new Term();

		term.setName(parameterStartsWith.apply(req.getParameter("name")));
		term.setLabel(parameterStartsWith.apply(req.getParameter("label")));
		term.setPurpose(paramaterContains.apply(req.getParameter("purpose")));
		String categoryName = req.getParameter("category").trim();
		if (!"".equals(categoryName)) {
			Category category = new Category();
			category.setName(categoryName);
			term.setCategory(category);
		}

		List<Term> terms = TermPersistenceService.search(term);
		req.setAttribute("foundTerms", terms);
		req.setAttribute("categories",
				categoryPersistenceService.listAll().stream().map(c -> c.getName()).collect(Collectors.toList()));
		req.getRequestDispatcher("/WEB-INF/jsp/TermSearch.jsp").forward(req, resp);
	}
}
