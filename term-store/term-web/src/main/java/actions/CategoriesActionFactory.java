package actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.CategoryPersistenceService;
import persistence.TermPersistenceService;
import persistence.beans.Category;
import persistence.beans.Term;
import persistence.exceptions.PersistenceServiceException;

public class CategoriesActionFactory implements ServletActionFactory {
	private CategoryPersistenceService categoryPersistenceService = new CategoryPersistenceService();
	private TermPersistenceService termPersistenceService = new TermPersistenceService();

	private Map<String, ServletAction> actionsMap = new HashMap<>();
	private Map<String, Boolean> yesNoMap = new HashMap<>();

	private ServletAction gotoTermCategoryListJsp = (req, resp) -> {
		req.setAttribute("categoryList", categoryPersistenceService.listAll());

		try {
			req.getRequestDispatcher("/WEB-INF/jsp/TermCategoryList.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	};

	private ServletAction saveCategory = (req, resp) -> {
		Category category = createCategoryFromRequest(req);

		try {
			categoryPersistenceService.save(category);
			redirectToCategoriesServlet(req, resp);
		} catch (PersistenceServiceException e) {
			viewException(req, resp, e);
		}
	};

	private ServletAction deleteCategory = (req, resp) -> {
		try {
			ensureRequestModificationNotForRoot(req, resp);

			Category category = new Category();
			category.setName(req.getParameter("name"));

			Term term = new Term();
			term.setCategory(category);
			if (termPersistenceService.search(term).isEmpty()) {
				categoryPersistenceService.delete(category);
				redirectToCategoriesServlet(req, resp);
			} else {
				viewException(req, resp,
						new PersistenceServiceException("Cannot delete category, since it contains terms."));
			}
		} catch (PersistenceServiceException e) {
			viewException(req, resp, e);
		}
	};

	private ServletAction updateCategory = (req, resp) -> {
		try {
			ensureRequestModificationNotForRoot(req, resp);

			Category newCategory = createCategoryFromRequest(req);
			Category oldCategory = new Category();
			oldCategory.setName(req.getParameter("oldName"));

			categoryPersistenceService.update(oldCategory, newCategory);

			redirectToCategoriesServlet(req, resp);
		} catch (PersistenceServiceException e) {
			viewException(req, resp, e);
		}
	};

	private ServletAction forwardEditCategoryForm = (req, resp) -> {
		Category category = createCategoryFromRequest(req);

		req.setAttribute("category", category);

		try {
			req.getRequestDispatcher("/WEB-INF/jsp/TermCategoryForm.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	};

	public CategoriesActionFactory() {
		yesNoMap.put("Yes", Boolean.TRUE);
		yesNoMap.put("No", Boolean.FALSE);

		actionsMap.put("saveCategory", saveCategory);
		actionsMap.put("gotoTermCategoryListJsp", gotoTermCategoryListJsp);
		actionsMap.put("deleteCategory", deleteCategory);
		actionsMap.put("forwardEditCategoryForm", forwardEditCategoryForm);
		actionsMap.put("updateCategory", updateCategory);
	}

	@Override
	public ServletAction getAction(HttpServletRequest req) {
		return actionsMap.get(req.getParameter("action"));
	}

	public ServletAction getAction(String action) {
		return actionsMap.get(action);
	}

	private Category createCategoryFromRequest(HttpServletRequest req) {
		Category category = new Category();
		category.setName(req.getParameter("name"));
		category.setChildrenPermitted(yesNoMap.get(req.getParameter("childrenPermitted")));
		return category;
	}

	private void viewException(HttpServletRequest req, HttpServletResponse resp, PersistenceServiceException e) {
		req.setAttribute("showExceptionModal", "Yes");
		req.setAttribute("exceptionMessage", e.getMessage());

		gotoTermCategoryListJsp.doAction(req, resp);
	}

	private void ensureRequestModificationNotForRoot(HttpServletRequest req, HttpServletResponse resp)
			throws PersistenceServiceException {
		if ("root".equalsIgnoreCase(req.getParameter("name"))) {
			throw new PersistenceServiceException("ROOT category cannot be modified.");
		}
	}

	private void redirectToCategoriesServlet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.sendRedirect(req.getContextPath() + "/categories");
		} catch (IOException e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	}
}
