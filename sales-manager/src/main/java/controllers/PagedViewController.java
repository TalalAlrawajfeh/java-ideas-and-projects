package controllers;

import constants.PaginationUseCasesParameters;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by u624 on 3/31/17.
 */
public class PagedViewController {
    private static final int SECTION_LENGTH = 5;
    private static final String IS_LAST_SECTION_OF_PAGES_ATTRIBUTE = "isLastSectionOfPages";
    private static final String MIN_PAGE_NUMBER_ATTRIBUTE = "minPageNumber";
    private static final String MAX_PAGE_NUMBER_ATTRIBUTE = "maxPageNumber";
    private static final String PAGE_ENTITIES_ATTRIBUTE = "pageEntities";
    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String SHOW_ERROR_ATTRIBUTE = "showError";

    protected int getPageNumber(Integer pageNumber) {
        int page = 1;
        if (Objects.nonNull(pageNumber)) {
            page = pageNumber;
        }
        return page;
    }

    protected void addModelPaginationAttributes(int pageNumber, Map<PaginationUseCasesParameters, Object> parametersMap, ModelAndView modelAndView) {
        int totalNumberOfPages = getTotalNumberOfPages(parametersMap);
        if (isShowErrorAttributeFalse(modelAndView) && 0 < totalNumberOfPages && pageNumber <= totalNumberOfPages) {
            Map<String, Object> model = modelAndView.getModel();
            int minPageNumber = getMinPageNumber(pageNumber);
            model.put(IS_LAST_SECTION_OF_PAGES_ATTRIBUTE, pageNumber >= getMinPageNumber(totalNumberOfPages));
            model.put(PAGE_NUMBER_ATTRIBUTE, pageNumber);
            model.put(MIN_PAGE_NUMBER_ATTRIBUTE, minPageNumber);
            model.put(MAX_PAGE_NUMBER_ATTRIBUTE, getMaxPageNumber(totalNumberOfPages, minPageNumber));
            model.put(PAGE_ENTITIES_ATTRIBUTE, getPageItemsList(parametersMap));
        }
    }

    protected boolean isShowErrorAttributeFalse(ModelAndView modelAndView) {
        return !(boolean) modelAndView.getModel().get(SHOW_ERROR_ATTRIBUTE);
    }

    private <T> List<T> getPageItemsList(Map<PaginationUseCasesParameters, Object> parametersMap) {
        return (List<T>) parametersMap.get(PaginationUseCasesParameters.PAGE_ITEMS_LIST);
    }

    private int getTotalNumberOfPages(Map<PaginationUseCasesParameters, Object> parametersMap) {
        return (int) parametersMap.get(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES);
    }

    private int getMaxPageNumber(int totalNumberOfPages, int minPageNumber) {
        int maxPageNumber = minPageNumber + SECTION_LENGTH - 1;
        if (maxPageNumber > totalNumberOfPages) {
            maxPageNumber = totalNumberOfPages;
        }
        return maxPageNumber;
    }

    private int getMinPageNumber(int pageNumber) {
        return getPagesSection(pageNumber) * SECTION_LENGTH + 1;
    }

    private int getPagesSection(int pageNumber) {
        return (int) Math.floor((float) (pageNumber - 1) / (float) SECTION_LENGTH);
    }
}
