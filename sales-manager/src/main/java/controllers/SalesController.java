package controllers;

import constants.PaginationUseCasesParameters;
import interactors.ListReceiptsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by u624 on 3/31/17.
 */
@Controller
public class SalesController extends PagedViewController {
    private static final String MANAGEMENT_SALES_VIEW_NAME = "management-sales";
    private static final String SALES_URL = "/sales";

    @Autowired
    private ListReceiptsUseCase listReceiptsUseCase;

    @RequestMapping(path = SALES_URL, method = RequestMethod.GET)
    public ModelAndView getSalesModelAndView(Integer pageNumber) {
        int page = getPageNumber(pageNumber);
        Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap = new EnumMap<>(PaginationUseCasesParameters.class);
        paginationUseCasesParametersMap.put(PaginationUseCasesParameters.PAGE_NUMBER, page);
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Map<PaginationUseCasesParameters, Object>>()
                .setUseCase(listReceiptsUseCase)
                .setUseCaseParameter(paginationUseCasesParametersMap)
                .setSuccessViewName(MANAGEMENT_SALES_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_SALES_VIEW_NAME)
                .executeUseCaseAndBuild();
        addModelPaginationAttributes(page, paginationUseCasesParametersMap, modelAndView);
        return modelAndView;
    }
}
