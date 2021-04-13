package interactors;

import beans.Product;
import beans.builders.ProductBuilder;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by u624 on 4/1/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddProductsUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AddProductUseCase addProductUseCase = new AddProductUseCase();

    private Product product;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        Mockito.doReturn(null)
                .when(productRepository)
                .findByCode(Matchers.anyString());
        product = getValidProduct();
    }

    @Test
    public void GivenProductWithInvalidCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        product.setCode("1 23");
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenProductWithInvalidDescriptionThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The description field is not valid");
        product.setDescription("###");
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenProductWithNoPriceThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The price field is not valid");
        product.setPrice(null);
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenProductWithNoQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The quantity field is not valid");
        product.setQuantityRemaining(null);
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenProductWithNoQuantitySoldThenNoExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenProductWithCodeThatAlreadyExistsThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Another product with the same code already exists");
        ProductEntity productEntity = product.convert();
        productEntity.setQuantitySold(0L);
        Mockito.doReturn(productEntity)
                .when(productRepository)
                .findByCode("123");
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenAnyProductThenQuantitySoldMustBeSetToZeroBeforePersisted() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
        product.setQuantitySold(5L);
        addProductUseCase.execute(product);
        ProductEntity productEntity = product.convert();
        productEntity.setQuantitySold(0L);
        assertTrue(productEntity.equals(productEntities.get(0)));
    }

    @Test
    public void GivenValidProductThenExactlyOneProductEntityShouldBePersisted() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
        addProductUseCase.execute(product);
        assertEquals(1, productEntities.size());
    }

    private Product getValidProduct() {
        return new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .build();
    }

    private void expectUseCaseException(String expectedMessage) {
        expectedException.expect(UseCaseException.class);
        expectedException.expectMessage(expectedMessage);
    }
}
