package interactors;

import beans.Product;
import beans.builders.ProductBuilder;
import constants.PaginationUseCasesParameters;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ProductRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListProductsUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ListProductsUseCase listProductsUseCase = new ListProductsUseCase();
    Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersObjectMap;

    @Before
    public void setup() {
        paginationUseCasesParametersObjectMap = new EnumMap<>(PaginationUseCasesParameters.class);
    }

    @Test
    public void WhenNoProductsExistThenTotalNumberOfPagesShouldBeZeroAndPageItemsNull() throws Exception {
        Mockito.doReturn(null).when(productRepository).findAll();
        executeListProductsUseCase(1);
        assertEquals(0, getTotalNumberOfPages());
        assertEquals(null, getPageItemsList());
    }

    @Test
    public void GivenPageNumberGreaterThanNumberOfPagesThenPageItemsListShouldBeEmpty() throws Exception {
        Mockito.doReturn(Collections.singletonList(new ProductEntityBuilder()
                .setCode("A")
                .build())).when(productRepository).findAll();
        executeListProductsUseCase(2);
        assertEquals(0, getPageItemsList().size());
    }

    @Test
    public void GivenUnorderedProductsListThenProductsShouldBeOrderedCorrectly() throws Exception {
        Mockito.doReturn(getUnorderedProductEntities()).when(productRepository).findAll();
        executeListProductsUseCase(1);
        List<Product> expectedOrderedProducts = getOrderedProducts();
        List<Product> actualOrderedProducts = getPageItemsList();
        for (int i = 0; i < expectedOrderedProducts.size(); i++) {
            assertEquals(expectedOrderedProducts.get(i).getCode(),
                    actualOrderedProducts.get(i).getCode());
        }
    }

    @Test
    public void Given140ProductsThenThereShouldBe3PagesAndAllPagesShouldCountainExactly50PagesExceptLastPageShouldContainAtMost50Products() throws Exception {
        Mockito.doReturn(get140ProductEntities()).when(productRepository).findAll();
        executeListProductsUseCase(1);
        assertEquals(3, getTotalNumberOfPages());
        assertEquals(50, getPageItemsCount());
        executeListProductsUseCase(2);
        assertEquals(50, getPageItemsCount());
        executeListProductsUseCase(3);
        assertEquals(40, getPageItemsCount());
    }

    @Test
    public void GivenProductsWithDifferentCodesThenPagesShouldContainProductsInCorrectPositions() throws Exception {
        Mockito.doReturn(getProductEntitiesWithDifferentCodes()).when(productRepository).findAll();
        executeListProductsUseCase(1);
        List<Product> products = getPageItemsList();
        assertEquals("A", products.get(0).getCode());
        assertEquals("B", products.get(1).getCode());
        assertEquals("B", products.get(48).getCode());
        assertEquals("C", products.get(49).getCode());
        executeListProductsUseCase(2);
        products = getPageItemsList();
        assertEquals("D", products.get(0).getCode());
        assertEquals("E", products.get(1).getCode());
        assertEquals("E", products.get(48).getCode());
        assertEquals("F", products.get(49).getCode());
        executeListProductsUseCase(3);
        products = getPageItemsList();
        assertEquals("G", products.get(0).getCode());
        assertEquals("H", products.get(1).getCode());
        assertEquals("H", products.get(48).getCode());
        assertEquals("I", products.get(49).getCode());
    }

    private int getTotalNumberOfPages() {
        return (int) paginationUseCasesParametersObjectMap.get(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES);
    }

    private void executeListProductsUseCase(int pageNumber) throws UseCaseException {
        paginationUseCasesParametersObjectMap.put(PaginationUseCasesParameters.PAGE_NUMBER, pageNumber);
        listProductsUseCase.execute(paginationUseCasesParametersObjectMap);
    }

    private List<Product> getPageItemsList() {
        return (List<Product>) paginationUseCasesParametersObjectMap.get(PaginationUseCasesParameters.PAGE_ITEMS_LIST);
    }

    private int getPageItemsCount() {
        return getPageItemsList().size();
    }

    private Iterable<ProductEntity> getUnorderedProductEntities() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(getProductEntity("F"));
        products.add(getProductEntity("B"));
        products.add(getProductEntity("C"));
        products.add(getProductEntity("A"));
        products.add(getProductEntity("E"));
        products.add(getProductEntity("D"));
        return products;
    }

    private List<Product> getOrderedProducts() {
        List<Product> products = new ArrayList<>();
        products.add(getProduct("A"));
        products.add(getProduct("B"));
        products.add(getProduct("C"));
        products.add(getProduct("D"));
        products.add(getProduct("E"));
        products.add(getProduct("F"));
        return products;
    }

    private Iterable<ProductEntity> getProductEntitiesWithDifferentCodes() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(getProductEntity("A"));
        products.add(getProductEntity("C"));
        products.add(getProductEntity("D"));
        products.add(getProductEntity("F"));
        products.add(getProductEntity("G"));
        products.add(getProductEntity("I"));
        add48ProductEntities(products, "B");
        add48ProductEntities(products, "E");
        add48ProductEntities(products, "H");
        return products;
    }

    private ProductEntity getProductEntity(String code) {
        return new ProductEntityBuilder()
                .setCode(code)
                .build();
    }


    private Product getProduct(String code) {
        return new ProductBuilder()
                .setCode(code)
                .build();
    }

    private Iterable<ProductEntity> get140ProductEntities() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 1; i <= 140; i++) {
            products.add(getProductEntity("A"));
        }
        return products;
    }


    private void add48ProductEntities(List<ProductEntity> products, String code) {
        for (int i = 2; i <= 49; i++) {
            products.add(getProductEntity(code));
        }
    }
}
