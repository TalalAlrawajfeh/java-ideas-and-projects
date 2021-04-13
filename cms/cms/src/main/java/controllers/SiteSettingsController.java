package controllers;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import beans.SiteSettings;
import usecases.SiteSettingsUseCase;
import usecases.exceptions.SiteSettingsValidationException;
import usecases.exceptions.SiteSettingsValidationException.SiteSettingsValidationExceptionCause;

@Controller
public class SiteSettingsController {
	private static final String INVALID_WEBSITE_DELIVERY_URL_ERROR_MESSAGE = "Invalid website delivery url";
	private static final String INVALID_WEBSITE_LOGO_ERROR_MESSAGE = "Invalid website logo";
	private static final String INVALID_WEBSITE_NAME_ERROR_MESSAGE = "Invalid website name";
	private static final String AN_ERROR_OCCURRED_ERROR_MESSAGE = "An error occurred";
	private static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	private static final String SITE_SETTINGS_ATTRIBUTE = "siteSettings";
	private static final String INCLUDED_PAGE_ATTRIBUTE = "includedPage";
	private static final String REDIRECT_SITE_SETTINGS = "redirect:/site-settings";
	private static final String SHOW_ERROR_ATTRIBUTE = "showError";
	private static final String SITE_SETTINGS_JSP = "SiteSettings";
	private static final String IMAGE_ATTRIBUTE = "image";
	private static final String BASE_JSP = "Base";

	private static Logger logger = Logger.getLogger(SiteSettingsController.class);

	@Autowired
	private SiteSettingsUseCase siteSettingsUseCase;

	private EnumMap<SiteSettingsValidationExceptionCause, String> errorMessageMap = new EnumMap<>(
			SiteSettingsValidationExceptionCause.class);

	public SiteSettingsController() {
		errorMessageMap.put(SiteSettingsValidationExceptionCause.INVALID_DELIVERY_URL,
				INVALID_WEBSITE_DELIVERY_URL_ERROR_MESSAGE);
		errorMessageMap.put(SiteSettingsValidationExceptionCause.INVALID_NAME, INVALID_WEBSITE_NAME_ERROR_MESSAGE);
		errorMessageMap.put(SiteSettingsValidationExceptionCause.INVALID_LOGO, INVALID_WEBSITE_LOGO_ERROR_MESSAGE);
		errorMessageMap.put(SiteSettingsValidationExceptionCause.OTHER, AN_ERROR_OCCURRED_ERROR_MESSAGE);
	}

	@RequestMapping(value = "/site-settings", method = RequestMethod.GET)
	public ModelAndView getSiteSettings(HttpServletRequest req, HttpServletResponse resp) {
		setProperAttributes(req, null);
		return new ModelAndView(BASE_JSP);
	}

	@RequestMapping(value = "/site-settings", method = RequestMethod.POST)
	public ModelAndView saveSiteSettings(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam String oldSiteSettings, @RequestParam String name, @RequestParam String deliveryUrl,
			@RequestPart MultipartFile logo) {

		try {
			SiteSettings siteSettings = siteSettingsUseCase.validateSiteSettings(
					ensureSeperatorExistsAtBeginning(deliveryUrl) + deliveryUrl, name, logo.getBytes());

			if (!"".equals(oldSiteSettings)) {
				siteSettingsUseCase.deleteSiteSettings(oldSiteSettings);
			}

			siteSettingsUseCase.saveSiteSettings(siteSettings);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);

			setProperAttributes(req, e.getMessage());
			return new ModelAndView(BASE_JSP);
		} catch (SiteSettingsValidationException e) {
			String errorMessage = errorMessageMap.get(e.getSiteSettingsValidationExceptionCause());
			logger.info(errorMessage, e);

			setProperAttributes(req, errorMessage);
			return new ModelAndView(BASE_JSP);
		}

		return new ModelAndView(REDIRECT_SITE_SETTINGS);
	}

	private void setProperAttributes(HttpServletRequest req, String errorMessage) {
		req.setAttribute(INCLUDED_PAGE_ATTRIBUTE, SITE_SETTINGS_JSP);
		SiteSettings siteSettings = siteSettingsUseCase.getSiteSettings();

		if (Objects.nonNull(siteSettings)) {
			req.setAttribute(SITE_SETTINGS_ATTRIBUTE, siteSettings);
			req.setAttribute(IMAGE_ATTRIBUTE, DatatypeConverter.printBase64Binary(siteSettings.getLogo()));
		}

		if (Objects.nonNull(errorMessage)) {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, true);
			req.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
		} else {
			req.setAttribute(SHOW_ERROR_ATTRIBUTE, false);
		}
	}

	private String ensureSeperatorExistsAtBeginning(String uri) {
		return uri.charAt(0) == '/' ? "" : "/";
	}
}
