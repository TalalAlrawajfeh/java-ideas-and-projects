package controllers;

import adapters.UseCase;
import beans.Pair;
import beans.Product;
import beans.Receipt;
import beans.builders.ProductBuilder;
import beans.builders.ReceiptBuilder;
import constants.PaginationUseCasesParameters;
import interactors.AddReceiptUseCase;
import interactors.EditReceiptUseCase;
import interactors.ListAllProductsUseCase;
import interactors.ListReceiptsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import utilities.DateUtility;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by u624 on 3/25/17.
 */
@Controller
public class ReceiptsController extends PagedViewController {
    private static final String MANAGEMENT_RECEIPTS_VIEW_NAME = "management-receipts";
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";
    private static final String RECEIPTS_URL = "/receipts";
    private static final String EDIT_ACTION = "edit";
    private static final String ADD_ACTION = "add";

    @Autowired
    private ListReceiptsUseCase listReceiptsUseCase;

    @Autowired
    private ListAllProductsUseCase listAllProductsUseCase;

    @Autowired
    private AddReceiptUseCase addReceiptUseCase;

    @Autowired
    private EditReceiptUseCase editReceiptUseCase;

    private UseCase<Pair<Map<PaginationUseCasesParameters, Object>, List<Product>>> listAllReceiptsAndProductsUseCase = p -> {
        listReceiptsUseCase.execute(p.getFirst());
        listAllProductsUseCase.execute(p.getSecond());
    };

    private Map<String, UseCase<Pair<Long, Receipt>>> actionUseCaseMap = new HashMap<>();

    public ReceiptsController() {
        actionUseCaseMap.put(ADD_ACTION, p -> {
            p.getSecond().setDate(DateUtility.formatDate(new Date()));
            addReceiptUseCase.execute(p.getSecond());
        });
        actionUseCaseMap.put(EDIT_ACTION, p -> {
            Receipt receipt = p.getSecond();
            receipt.setId(p.getFirst());
            editReceiptUseCase.execute(receipt);
        });
    }

    @RequestMapping(path = RECEIPTS_URL, method = RequestMethod.GET)
    public ModelAndView getReceiptsModelAndView(Integer pageNumber) {
        int page = getPageNumber(pageNumber);
        List<Product> products = new ArrayList<>();
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put(PRODUCTS_ATTRIBUTE_NAME, products);
        Map<PaginationUseCasesParameters, Object> parametersMap = new EnumMap<>(PaginationUseCasesParameters.class);
        parametersMap.put(PaginationUseCasesParameters.PAGE_NUMBER, page);
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Pair<Map<PaginationUseCasesParameters, Object>, List<Product>>>()
                .setUseCase(listAllReceiptsAndProductsUseCase)
                .setUseCaseParameter(new Pair<>(parametersMap, products))
                .setSuccessViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .setModelMap(modelMap)
                .executeUseCaseAndBuild();
        addModelPaginationAttributes(page, parametersMap, modelAndView);
        return modelAndView;
    }

    @RequestMapping(path = RECEIPTS_URL, method = RequestMethod.POST)
    public ModelAndView addReceipt(@RequestParam String action, @RequestParam Long receiptId, @RequestParam String code, @RequestParam BigDecimal price, @RequestParam Long quantity, @RequestParam BigDecimal total) {
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Pair<Long, Receipt>>()
                .setUseCase(actionUseCaseMap.get(action))
                .setUseCaseParameter(new Pair<>(receiptId, new ReceiptBuilder()
                        .setProduct(new ProductBuilder().setCode(code).build())
                        .setPrice(price)
                        .setQuantity(quantity)
                        .setTotal(total)
                        .build()))
                .setSuccessViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .executeUseCaseAndBuild();
        addReceiptsAndProductsToModelAndView(modelAndView);
        return modelAndView;
    }

    private void addReceiptsAndProductsToModelAndView(ModelAndView modelAndView) {
        if (isShowErrorAttributeFalse(modelAndView)) {
            modelAndView.getModel().putAll(getReceiptsModelAndView(1).getModel());
        }
    }
}
