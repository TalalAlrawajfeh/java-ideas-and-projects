package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import usecases.EditPageUseCase;

@Controller
public class PublishPageController {
	private static final String REDIRECT_PAGE_MANAGEMENT = "redirect:/page-management?filter=all";
	private static final String PUBLISH_PAGE_URL = "/publish-page";

	@Autowired
	EditPageUseCase editPageUseCase;

	@RequestMapping(value = PUBLISH_PAGE_URL, method = RequestMethod.GET)
	public ModelAndView publishPage(@RequestParam String uri) {
		editPageUseCase.publishPage(uri);
		
		return new ModelAndView(REDIRECT_PAGE_MANAGEMENT);
	}
}
