package interactors;

import beans.Pair;
import beans.Product;
import beans.builders.ProductBuilder;
import entities.ProductEntity;
import entities.ReceiptEntity;
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
import persistence.ReceiptRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by u624 on 4/2/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class EditProductUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    private Product product;
    private String oldCode;

    @InjectMocks
    private EditProductUseCase editProductUseCase = new EditProductUseCase();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        product = getValidProduct();
        oldCode = "321";
    }

    @Test
    public void GivenProductWithInvalidCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        product.setCode("1 23");
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenProductWithInvalidDescriptionThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The description field is not valid");
        product.setDescription("\0");
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenProductWithNoPriceThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The price field is not valid");
        product.setPrice(null);
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenProductWithNoQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The quantity field is not valid");
        product.setQuantityRemaining(null);
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenProductWithNoQuantitySoldThenNoExceptionShouldBeThrown() throws Exception {
        Mockito.doReturn(product.convert()).when(productRepository).findByCode("321");
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenProductWithNewCodeAndWithChildReceiptsThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("There are receipts that depend on this product");
        Mockito.doReturn(product.convert()).when(productRepository).findByCode("321");
        Mockito.doReturn(Arrays.asList(new ReceiptEntity())).when(receiptRepository).findByProductEntity(product.convert());
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenProductWithNewCodeThatAlreadyExistsThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Another product with the same code already exists");
        Mockito.doReturn(product.convert()).when(productRepository).findByCode("321");
        Mockito.doReturn(new ProductEntity()).when(productRepository).findByCode("123");
        editProductUseCase.execute(new Pair<>(oldCode, product));
    }

    @Test
    public void GivenValidProductThenOneProductEntityShouldBePersistedAndShouldHaveTheSameQuantitySold() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        product.setQuantitySold(10L);
        Mockito.doReturn(product.convert()).when(productRepository).findByCode("321");
        Mockito.doReturn(null).when(productRepository).findByCode("123");
        Mockito.doAnswer(invocationOnMock -> productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]))
                .when(productRepository)
                .save(Matchers.<ProductEntity>any());
        editProductUseCase.execute(new Pair<>(oldCode, product));
        assertEquals(1, productEntities.size());
        assertEquals(10L, (long) productEntities.get(0).getQuantitySold());
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
