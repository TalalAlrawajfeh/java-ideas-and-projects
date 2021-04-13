package controllers;

import java.util.EnumMap;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import beans.Page;
import beans.Site;
import usecases.AddSiteUseCase;
import usecases.EditSiteUseCase;
import usecases.PageManagementUseCase;
import usecases.exceptions.SiteValidationException;
import usecases.exceptions.SiteValidationException.SiteValidationExceptionCause;

@Controller
public class EditSiteController {
	private static final String INVALID_SITE_NAME_ERROR_MESSAGE = "Invalid site name";
	private static final String INVALID_SITE_URI_ERROR_MESSAGE = "Invalid site uri";
	private static final String REDIRECT_SITE_MANAGEMENT = "redirect:/site-management";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String OTHER_ERROR_MESSAGE = "An error occured";
	private static final String PAGES_ATTRIBUTE = "pages";
	private static final String SITE_ATTRIBUTE = "site";
	private static final String EDIT_SITE_JSP = "EditSite";
	private static final String EDIT_SITE_URL = "/edit-site";
	private static final String BASE_JSP = "Base";
	private static final String ROOT_URL = "/root";

	@Autowired
	private AddSiteUseCase addSiteUseCase;

	@Autowired
	private EditSiteUseCase editSiteUseCase;

	@Autowired
	private PageManagementUseCase pageManagementUseCase;

	private EnumMap<SiteValidationExceptionCause, String> errorMessageMap = new EnumMap<>(
			SiteValidationExceptionCause.class);

	public EditSiteController() {
		errorMessageMap.put(SiteValidationExceptionCause.INVALID_NAME, INVALID_SITE_NAME_ERROR_MESSAGE);
		errorMessageMap.put(SiteValidationExceptionCause.INVALID_URI, INVALID_SITE_URI_ERROR_MESSAGE);
		errorMessageMap.put(SiteValidationExceptionCause.OTHER, OTHER_ERROR_MESSAGE);
	}

	@RequestMapping(value = EDIT_SITE_URL, method = RequestMethod.GET)
	public ModelAndView editSite(HttpServletRequest req, HttpServletResponse resp, @RequestParam String uri) {
		if (ROOT_URL.equals(uri)) {
			return new ModelAndView(REDIRECT_SITE_MANAGEMENT);
		}

		setProperAttributes(req, uri, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = EDIT_SITE_URL, method = RequestMethod.POST)
	public ModelAndView editSite(HttpServletRequest req, HttpServletResponse resp, @RequestParam String uri,
			@RequestParam String name, @RequestParam String landingPage) {

		try {
			addSiteUseCase.validateSite(name, uri);
			saveNewSite(uri, name, landingPage);
		} catch (SiteValidationException e) {
			String errorMessage = errorMessageMap.get(e.getSiteValidationExceptionCause());
			Logger logger = Logger.getLogger(EditSiteController.class);
			logger.info(errorMessage, e);

			setProperAttributes(req, uri, errorMessage);
			return new ModelAndView(BASE_JSP);
		}

		return new ModelAndView(REDIRECT_SITE_MANAGEMENT);
	}

	private void saveNewSite(String uri, String name, String landingPage) {
		Site site = editSiteUseCase.getSiteByUri(uri);
		Page page = pageManagementUseCase.getPageByUri(landingPage);

		site.setName(name);
		site.setLandingPage(page);
		editSiteUseCase.updateSite(site);
	}

	private void setProperAttributes(HttpServletRequest req, String uri, String errorMessage) {
		req.setAttribute(SITE_ATTRIBUTE, editSiteUseCase.getSiteByUri(uri));
		req.setAttribute(PAGES_ATTRIBUTE, pageManagementUseCase.getAllPages());
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, EDIT_SITE_JSP);

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}
}
