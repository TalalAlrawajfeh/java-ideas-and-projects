package controllers;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import beans.builders.PageBuilder;
import usecases.AddPageUseCase;
import usecases.EditSiteUseCase;
import usecases.SiteManagementUseCase;

@Controller
public class AddPageController {
	private static final String DUPLICATE_PAGE_URI_ERROR_MESSAGE = "A page with the same uri already exists";
	private static final String INVALID_PAGE_URI_ERROR_MESSAGE = "Invalid page URI";
	private static final String REDIRECT_PAGE_MANAGEMENT = "redirect:/page-management?filter=all";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String SITES_ATTRIBUTE = "sites";
	private static final String ADD_PAGE_JSP = "AddPage";
	private static final String ADD_PAGE_URL = "/add-page";
	private static final String BASE_JSP = "Base";

	@Autowired
	private SiteManagementUseCase siteManagementUseCase;

	@Autowired
	private EditSiteUseCase editSiteUseCase;

	@Autowired
	private AddPageUseCase addPageUseCase;

	@RequestMapping(value = ADD_PAGE_URL, method = RequestMethod.GET)
	public ModelAndView addPage(HttpServletRequest req, HttpServletResponse resp) {
		setProperAttributes(req, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = ADD_PAGE_URL, method = RequestMethod.POST)
	public ModelAndView addPage(HttpServletRequest req, HttpServletResponse resp, @RequestParam String title,
			@RequestParam String uri, @RequestParam String site, @RequestParam String seo,
			@RequestParam String content) {

		String pageUri = site + ensureSeperatorExistsAtBeginning(uri) + uri;

		if (!addPageUseCase.isPageUriValid(pageUri)) {
			setProperAttributes(req, INVALID_PAGE_URI_ERROR_MESSAGE);
			return new ModelAndView(BASE_JSP);
		}

		if (addPageUseCase.pageExists(pageUri)) {
			setProperAttributes(req, DUPLICATE_PAGE_URI_ERROR_MESSAGE);
			return new ModelAndView(BASE_JSP);
		}

		savePage(title, site, seo, content, pageUri);

		return new ModelAndView(REDIRECT_PAGE_MANAGEMENT);
	}

	private void savePage(String title, String site, String seo, String content, String pageUri) {
		addPageUseCase.savePage(new PageBuilder().setTitle(title).setUri(pageUri).setIsHtml(true).setSeo(seo)
				.setContent(content).setIsPublished(false).setSite(editSiteUseCase.getSiteByUri(site)).build());
	}

	private String ensureSeperatorExistsAtBeginning(String uri) {
		return uri.charAt(0) == '/' ? "" : "/";
	}

	private void setProperAttributes(HttpServletRequest req, String errorMessage) {
		req.setAttribute(SITES_ATTRIBUTE, siteManagementUseCase.getAllSites());
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, ADD_PAGE_JSP);

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}
}
