package controllers;

import adapters.UseCase;
import exceptions.UseCaseException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by u624 on 3/27/17.
 */
public class UseCaseModelAndViewBuilder<T> {
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String SHOW_ERROR_ATTRIBUTE_NAME = "showError";

    private final Logger logger = Logger.getLogger(UseCaseModelAndViewBuilder.class);

    private UseCase<T> useCase;
    private T useCaseParameter;
    private String successViewName;
    private String errorViewName;
    private Map<String, Object> modelMap;

    public UseCaseModelAndViewBuilder<T> setUseCase(UseCase<T> useCase) {
        this.useCase = useCase;
        return this;
    }

    public UseCaseModelAndViewBuilder<T> setUseCaseParameter(T useCaseParameter) {
        this.useCaseParameter = useCaseParameter;
        return this;
    }

    public UseCaseModelAndViewBuilder<T> setSuccessViewName(String successViewName) {
        this.successViewName = successViewName;
        return this;
    }

    public UseCaseModelAndViewBuilder<T> setErrorViewName(String errorViewName) {
        this.errorViewName = errorViewName;
        return this;
    }

    public UseCaseModelAndViewBuilder<T> setModelMap(Map<String, Object> modelMap) {
        this.modelMap = modelMap;
        return this;
    }

    public ModelAndView executeUseCaseAndBuild() {
        try {
            useCase.execute(useCaseParameter);
        } catch (UseCaseException e) {
            logger.debug(e);
            return getErrorModelAndView(e);
        }
        return new ModelAndView(successViewName, getModel());
    }

    private Map<String, Object> getModel() {
        Map<String, Object> model = getErrorModel(false, null);
        if (Objects.nonNull(modelMap)) {
            model.putAll(modelMap);
        }
        return model;
    }

    private ModelAndView getErrorModelAndView(UseCaseException exception) {
        return new ModelAndView(errorViewName, getErrorModel(true, exception));
    }

    private Map<String, Object> getErrorModel(boolean showException, Exception exception) {
        Map<String, Object> errorModel = new HashMap<>();
        errorModel.put(SHOW_ERROR_ATTRIBUTE_NAME, showException);
        if (showException) {
            errorModel.put(ERROR_MESSAGE_ATTRIBUTE_NAME, exception.getMessage());
        }
        return errorModel;
    }
}
