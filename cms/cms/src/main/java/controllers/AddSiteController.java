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
import beans.builders.PageBuilder;
import beans.builders.SiteBuilder;
import usecases.AddPageUseCase;
import usecases.AddSiteUseCase;
import usecases.SiteManagementUseCase;
import usecases.exceptions.SiteValidationException;
import usecases.exceptions.SiteValidationException.SiteValidationExceptionCause;

@Controller
public class AddSiteController {
	private static final String INVALID_SITE_NAME_ERROR_MESSAGE = "Invalid site name";
	private static final String INVALID_SITE_URI_ERROR_MESSAGE = "Invalid site URI";
	private static final String REDIRECT_SITE_MANAGEMENT = "redirect:/site-management";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String DUPLICATE_URI_MESSAGE = "A site with the same URI exists";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String OTHER_ERROR_MESSAGE = "An error occured";
	private static final String SITES_ATTRIBUTE = "sites";
	private static final String WELCOME_TITLE = "Welcome";
	private static final String ADD_SITE_JSP = "AddSite";
	private static final String ADD_SITE_URL = "/add-site";
	private static final String WELCOME_URI = "/welcome";
	private static final String BASE_JSP = "Base";

	@Autowired
	private SiteManagementUseCase siteManagementUseCase;

	@Autowired
	private AddSiteUseCase addSiteUseCase;

	@Autowired
	private AddPageUseCase addPageUseCase;

	private EnumMap<SiteValidationExceptionCause, String> errorMessageMap = new EnumMap<>(
			SiteValidationExceptionCause.class);

	public AddSiteController() {
		errorMessageMap.put(SiteValidationExceptionCause.INVALID_NAME, INVALID_SITE_NAME_ERROR_MESSAGE);
		errorMessageMap.put(SiteValidationExceptionCause.INVALID_URI, INVALID_SITE_URI_ERROR_MESSAGE);
		errorMessageMap.put(SiteValidationExceptionCause.OTHER, OTHER_ERROR_MESSAGE);
	}

	@RequestMapping(value = ADD_SITE_URL, method = RequestMethod.GET)
	public ModelAndView addSite(HttpServletRequest req, HttpServletResponse resp) {
		setProperAttributes(req, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = ADD_SITE_URL, method = RequestMethod.POST)
	public ModelAndView addSite(HttpServletRequest req, HttpServletResponse resp, @RequestParam String name,
			@RequestParam String uri, @RequestParam String parentSite) {

		try {
			String siteUri = parentSite + ensureSeperatorExistsAtBeginning(uri) + uri;
			Site site = addSiteUseCase.validateSite(name, siteUri);

			if (addSiteUseCase.siteExists(siteUri)) {
				setProperAttributes(req, DUPLICATE_URI_MESSAGE);
				return new ModelAndView(BASE_JSP);
			}

			saveSite(parentSite, site);
		} catch (SiteValidationException e) {
			String errorMessage = errorMessageMap.get(e.getSiteValidationExceptionCause());
			Logger logger = Logger.getLogger(AddSiteController.class);
			logger.info(errorMessage, e);

			setProperAttributes(req, errorMessage);
			return new ModelAndView(BASE_JSP);
		}

		return new ModelAndView(REDIRECT_SITE_MANAGEMENT);
	}

	private String ensureSeperatorExistsAtBeginning(String uri) {
		return uri.charAt(0) == '/' ? "" : "/";
	}

	private void saveSite(String parentSite, Site site) {
		site.setParentSite(new SiteBuilder().setUri(parentSite).build());
		addSiteUseCase.saveSite(site);

		Page page = new PageBuilder().setTitle(WELCOME_TITLE).setUri(site.getUri() + WELCOME_URI).setIsPublished(false)
				.build();
		page.setSite(site);
		addPageUseCase.savePage(page);

		site.setLandingPage(page);
		addSiteUseCase.saveSite(site);
	}

	private void setProperAttributes(HttpServletRequest req, String errorMessage) {
		req.setAttribute(SITES_ATTRIBUTE, siteManagementUseCase.getAllSites());
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, ADD_SITE_JSP);

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}
}
