package controllers;

import adapters.UseCase;
import beans.Pair;
import beans.Product;
import beans.builders.ProductBuilder;
import constants.PaginationUseCasesParameters;
import interactors.AddProductUseCase;
import interactors.EditProductUseCase;
import interactors.ListProductsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by u624 on 3/25/17.
 */
@Controller
public class ProductsController extends PagedViewController {
    private static final String MANAGEMENT_PRODUCTS_VIEW_NAME = "management-products";
    private static final String PRODUCTS_URL = "/products";
    private static final String EDIT_ACTION = "edit";
    private static final String ADD_ACTION = "add";

    @Autowired
    private ListProductsUseCase listProductsUseCase;

    @Autowired
    private AddProductUseCase addProductUseCase;

    @Autowired
    private EditProductUseCase editProductUseCase;

    private Map<String, UseCase<Pair<String, Product>>> actionUseCaseMap = new HashMap<>();

    public ProductsController() {
        actionUseCaseMap.put(ADD_ACTION, p -> addProductUseCase.execute(p.getSecond()));
        actionUseCaseMap.put(EDIT_ACTION, p -> editProductUseCase.execute(p));
    }

    @RequestMapping(path = PRODUCTS_URL, method = RequestMethod.GET)
    public ModelAndView getProductsModelAndView(Integer pageNumber) {
        int page = getPageNumber(pageNumber);
        Map<PaginationUseCasesParameters, Object> parametersMap = new EnumMap<>(PaginationUseCasesParameters.class);
        parametersMap.put(PaginationUseCasesParameters.PAGE_NUMBER, page);
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Map<PaginationUseCasesParameters, Object>>()
                .setUseCase(listProductsUseCase)
                .setUseCaseParameter(parametersMap)
                .setSuccessViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .executeUseCaseAndBuild();
        addModelPaginationAttributes(page, parametersMap, modelAndView);
        return modelAndView;
    }

    @RequestMapping(path = PRODUCTS_URL, method = RequestMethod.POST)
    public ModelAndView addProduct(@RequestParam String action, @RequestParam String oldCode, @RequestParam String code, @RequestParam String description, @RequestParam BigDecimal price, @RequestParam Long quantity) {
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Pair<String, Product>>()
                .setUseCase(actionUseCaseMap.get(action))
                .setUseCaseParameter(new Pair<>(oldCode, new ProductBuilder()
                        .setCode(code)
                        .setDescription(description)
                        .setPrice(price)
                        .setQuantityRemaining(quantity)
                        .build()))
                .setSuccessViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .executeUseCaseAndBuild();
        addProductsToModelAndView(modelAndView);
        return modelAndView;
    }

    private void addProductsToModelAndView(ModelAndView modelAndView) {
        if (isShowErrorAttributeFalse(modelAndView)) {
            modelAndView.getModel().putAll(getProductsModelAndView(1).getModel());
        }
    }
}
