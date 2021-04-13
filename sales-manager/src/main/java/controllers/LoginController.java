package controllers;

import adapters.UseCase;
import beans.Pair;
import beans.User;
import beans.builders.UserBuilder;
import interactors.LoginUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import utilities.HashingUtility;

/**
 * Created by u624 on 3/22/17.
 */
@Controller
@SessionAttributes("user")
public class LoginController {
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String SHOW_ERROR_ATTRIBUTE_NAME = "showError";
    private static final String MANAGEMENT_HOME_VIEW_NAME = "management-home";
    private static final String LOGIN_VIEW_NAME = "login";
    private static final String LOGIN_URL = "/login";

    @Autowired
    private LoginUseCase loginUseCase;

    private UseCase<Pair<Model, User>> loginSessionUseCase = p -> {
        User user = p.getSecond();
        loginUseCase.execute(user);
        p.getFirst().addAttribute(USER_SESSION_ATTRIBUTE_NAME, user);
    };

    @RequestMapping(path = LOGIN_URL, method = RequestMethod.GET)
    public ModelAndView getLoginModelAndView(Model model) {
        if (model.containsAttribute(USER_SESSION_ATTRIBUTE_NAME)) {
            return new ModelAndView(MANAGEMENT_HOME_VIEW_NAME);
        }
        return new ModelAndView(LOGIN_VIEW_NAME, SHOW_ERROR_ATTRIBUTE_NAME, false);
    }

    @RequestMapping(path = LOGIN_URL, method = RequestMethod.POST)
    public ModelAndView submitLoginForm(Model model, @RequestParam String username, @RequestParam String password) {
        return new UseCaseModelAndViewBuilder<Pair<Model, User>>()
                .setUseCase(loginSessionUseCase)
                .setUseCaseParameter(new Pair<>(model, new UserBuilder()
                        .setUsername(username)
                        .setPasswordHashCode(HashingUtility.hashString(password))
                        .build()))
                .setSuccessViewName(MANAGEMENT_HOME_VIEW_NAME)
                .setErrorViewName(LOGIN_VIEW_NAME)
                .executeUseCaseAndBuild();
    }
}