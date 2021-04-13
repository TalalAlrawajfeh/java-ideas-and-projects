package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by u624 on 3/24/17.
 */
@Controller
public class ManagementHomeController {
    private static final String MANAGEMENT_HOME_VIEW_NAME = "management-home";
    private static final String MANAGEMENT_HOME_URL = "/management-home";

    @RequestMapping(path = MANAGEMENT_HOME_URL, method = RequestMethod.GET)
    public String getManagementHomeModelAndView() {
        return MANAGEMENT_HOME_VIEW_NAME;
    }
}
