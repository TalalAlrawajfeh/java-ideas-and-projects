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

import beans.Page;
import beans.Site;
import beans.builders.PageBuilder;
import usecases.AddPageUseCase;
import usecases.EditPageUseCase;
import usecases.EditSiteUseCase;
import usecases.PageManagementUseCase;
import usecases.SiteManagementUseCase;

@Controller
public class EditPageController {
	private static final String INVALID_PAGE_URI_ERROR_MESSAGE = "Invalid page uri";
	private static final String DUPLICATE_URI_ERROR_MESSAGE = "A page with the same URI already exists";
	private static final String LANDING_PAGE_ERROR_MESSAGE = "A site has this page as its landing page";
	private static final String REDIRECT_PAGE_MANAGEMENT = "redirect:/page-management?filter=all";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String EDITED_PAGE_ATTRIBUTE = "editedPage";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String SITES_ATTRIBUTE = "sites";
	private static final String EDIT_SITE_JSP = "EditPage";
	private static final String EDIT_PAGE_URL = "/edit-page";
	private static final String BASE_JSP = "Base";

	@Autowired
	private SiteManagementUseCase siteManagementUseCase;

	@Autowired
	private EditSiteUseCase editSiteUseCase;

	@Autowired
	private PageManagementUseCase pageManagementUseCase;

	@Autowired
	private AddPageUseCase addPageUseCase;

	@Autowired
	private EditPageUseCase editPageUseCase;

	@RequestMapping(value = EDIT_PAGE_URL, method = RequestMethod.GET)
	public ModelAndView editPage(HttpServletRequest req, HttpServletResponse resp, @RequestParam String uri) {
		setProperAttributes(req, uri, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = EDIT_PAGE_URL, method = RequestMethod.POST)
	public ModelAndView editPage(HttpServletRequest req, HttpServletResponse resp, @RequestParam String editedPage,
			@RequestParam String title, @RequestParam String uri, @RequestParam String seo,
			@RequestParam String content) {

		Site site = pageManagementUseCase.getPageByUri(editedPage).getSite();
		String pageUri = site.getUri() + ensureSeperatorExistsAtBeginning(uri) + uri;

		if (!addPageUseCase.isPageUriValid(pageUri)) {
			setProperAttributes(req, editedPage, INVALID_PAGE_URI_ERROR_MESSAGE);
			return new ModelAndView(BASE_JSP);
		}

		if (!editedPage.equals(pageUri)) {
			if (addPageUseCase.pageExists(pageUri)) {
				setProperAttributes(req, editedPage, DUPLICATE_URI_ERROR_MESSAGE);
				return new ModelAndView(BASE_JSP);
			}

			if (isLandingPage(site, editedPage)) {
				setProperAttributes(req, editedPage, LANDING_PAGE_ERROR_MESSAGE);
				return new ModelAndView(BASE_JSP);
			}

			editPageUseCase.deletePageByUri(editedPage);
		}

		savePage(editedPage, title, seo, content, site, pageUri);
		return new ModelAndView(REDIRECT_PAGE_MANAGEMENT);
	}

	@RequestMapping(value = EDIT_PAGE_URL, method = RequestMethod.DELETE)
	public void deletePage(HttpServletRequest req, HttpServletResponse resp, @RequestParam String uri) {
		if (isLandingPage(pageManagementUseCase.getPageByUri(uri).getSite(), uri)) {
			resp.setStatus(400);
			return;
		}

		if (editPageUseCase.wasPublished(uri)) {
			editPageUseCase.deleteCorrespondingPublishedPage(uri);
		}

		editPageUseCase.deletePageByUri(uri);

		resp.setStatus(200);
	}

	private void savePage(String editedPage, String title, String seo, String content, Site site, String pageUri) {
		updatePage(editedPage, site, new PageBuilder().setTitle(title).setUri(pageUri).setIsHtml(true)
				.setContent(content).setSeo(seo).setIsPublished(false).setSite(site).build());
	}

	private void updatePage(String editedPageUri, Site siteOfPage, Page newPage) {
		if (isLandingPage(siteOfPage, editedPageUri)) {
			siteOfPage.setLandingPage(null);
			editSiteUseCase.updateSite(siteOfPage);

			addPageUseCase.savePage(newPage);

			siteOfPage.setLandingPage(newPage);
			editSiteUseCase.updateSite(siteOfPage);
		} else {
			addPageUseCase.savePage(newPage);
		}
	}

	private boolean isLandingPage(Site siteOfPage, String uri) {
		return siteOfPage.getLandingPage().getUri().equals(uri);
	}

	private String ensureSeperatorExistsAtBeginning(String uri) {
		return uri.charAt(0) == '/' ? "" : "/";
	}

	private void setProperAttributes(HttpServletRequest req, String uri, String errorMessage) {
		req.setAttribute(SITES_ATTRIBUTE, siteManagementUseCase.getAllSites());
		req.setAttribute(EDITED_PAGE_ATTRIBUTE, pageManagementUseCase.getPageByUri(uri));
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, EDIT_SITE_JSP);

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}
}
