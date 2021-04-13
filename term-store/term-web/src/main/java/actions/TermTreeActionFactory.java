package actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.CategoryPersistenceService;
import persistence.TermPersistenceService;
import persistence.beans.Category;
import persistence.beans.Term;
import persistence.exceptions.PersistenceServiceException;

public class TermTreeActionFactory implements ServletActionFactory {
	private CategoryPersistenceService categoryPersistenceService = new CategoryPersistenceService();
	private TermPersistenceService termPersistenceService = new TermPersistenceService();
	private Map<String, ServletAction> actionMap = new HashMap<>();
	private Map<Boolean, ServletAction> restfulActionMap = new HashMap<>();

	private ServletAction notRestful = (req, resp) -> {
		String displayParameter = req.getParameter("display");
		if (displayParameter != null) {
			Long id = Long.valueOf(displayParameter);
			Term term = termPersistenceService.searchById(id);
			req.setAttribute("displayTerm", term);
		}

		req.setAttribute("categories",
				categoryPersistenceService.listAll().stream().map(c -> c.getName()).collect(Collectors.toList()));
		try {
			req.getRequestDispatcher("/WEB-INF/jsp/TermTree.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	};

	private ServletAction restfulAction = (req, resp) -> {
		Term term = termPersistenceService.searchById(Long.valueOf(req.getParameter("id")));
		resp.setContentType("application/json");
		try {
			sendJSON(resp, term);
		} catch (Exception e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	};

	private ServletAction saveTerm = (req, resp) -> {
		try {
			Term term = getTermFromRequest(req);
			checkRootModificationRequest(term);
			termPersistenceService.update(term);
			redirectToTermTreeServlet(req, resp);
		} catch (PersistenceServiceException e) {
			viewException(req, resp, e);
		}
	};

	private ServletAction deleteTerm = (req, resp) -> {
		try {
			Term term = getTermFromRequest(req);
			checkRootModificationRequest(term);
			termPersistenceService.delete(term);
			redirectToTermTreeServlet(req, resp);
		} catch (PersistenceServiceException e) {
			viewException(req, resp, e);
		}
	};

	private ServletAction addChild = (req, resp) -> {
		Term child = new Term();
		Term parent = new Term();

		parent.setId(Long.valueOf(req.getParameter("parentId")));
		child.setName(req.getParameter("name"));
		child.setLabel(req.getParameter("label"));
		child.setPurpose(req.getParameter("purpose"));
		Category category = new Category();

		category.setName(req.getParameter("category"));
		child.setCategory(categoryPersistenceService.search(category));
		child.setParent(parent);

		if (termPersistenceService.checkIfTermNameExists(child)) {
			viewException(req, resp, new PersistenceServiceException("A sibling with the same name already exists."));
		} else {
			termPersistenceService.save(child);
			redirectToTermTreeServlet(req, resp);
		}
	};

	private void sendJSON(HttpServletResponse resp, Term term) throws IOException {
		PrintWriter writer = resp.getWriter();
		writer.print("{\"name\":\"");
		writer.print(term.getName());
		writer.print("\", \"label\":\"");
		writer.print(term.getLabel());
		writer.print("\", \"purpose\":\"");
		writer.print(term.getLabel());
		writer.print("\", \"category\":\"");
		writer.print(term.getCategory().getName());
		writer.print("\"}");
		writer.flush();
		resp.setStatus(200);
	}

	public TermTreeActionFactory() {
		actionMap.put("saveTerm", saveTerm);
		actionMap.put("deleteTerm", deleteTerm);
		actionMap.put("addChild", addChild);

		restfulActionMap.put(false, notRestful);
		restfulActionMap.put(true, restfulAction);
	}

	@Override
	public ServletAction getAction(HttpServletRequest req) {
		return actionMap.get(req.getParameter("action"));
	}

	public ServletAction getGETRestfulAction(HttpServletRequest req) {
		return restfulActionMap.get(Objects.nonNull(req.getParameter("id")));
	}

	private Term getTermFromRequest(HttpServletRequest req) {
		Term term = new Term();

		term.setId(Long.valueOf(req.getParameter("id")));
		term.setName(req.getParameter("name"));
		term.setLabel(req.getParameter("label"));
		term.setPurpose(req.getParameter("purpose"));

		Category category = new Category();
		category.setName(req.getParameter("category"));
		term.setCategory(category);
		return term;
	}

	private void redirectToTermTreeServlet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.sendRedirect(req.getContextPath() + "/term-tree");
		} catch (IOException e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	}

	private void checkRootModificationRequest(Term term) throws PersistenceServiceException {
		if ("root".equalsIgnoreCase(term.getName()) && "root".equalsIgnoreCase(term.getCategory().getName()))
			throw new PersistenceServiceException("The root term cannot be modified.");
	}

	private void viewException(HttpServletRequest req, HttpServletResponse resp, PersistenceServiceException e) {
		req.setAttribute("showExceptionModal", "Yes");
		req.setAttribute("exceptionMessage", e.getMessage());
		notRestful.doAction(req, resp);
	}
}
