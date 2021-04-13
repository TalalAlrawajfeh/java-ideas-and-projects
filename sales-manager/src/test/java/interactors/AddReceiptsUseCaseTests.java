package interactors;

import beans.Receipt;
import beans.builders.ProductBuilder;
import beans.builders.ReceiptBuilder;
import entities.ProductEntity;
import entities.ReceiptEntity;
import entities.builders.ProductEntityBuilder;
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
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by u624 on 4/1/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddReceiptsUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private AddReceiptUseCase addReceiptUseCase = new AddReceiptUseCase();

    private Receipt receipt;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        receipt = getValidReceipt();
    }

    @Test
    public void GivenReceiptWithNoProductThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        receipt.setProduct(null);
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithProductWithNoCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        receipt.getProduct().setCode(null);
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithProductWithInvalidCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        receipt.getProduct().setCode("1 23");
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithNoPriceThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The price field is not valid");
        receipt.setPrice(null);
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithNoQuantityThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The quantity field is not valid");
        receipt.setQuantity(null);
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithProductThatDoesNotExistThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Product doesn't exist");
        Mockito.doReturn(null).when(productRepository).findByCode("123");
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithQuantityNotSufficientWithRespectToProductQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Product quantity remaining is not sufficient");
        Mockito.doReturn(new ProductEntityBuilder()
                .setQuantityRemaining(0L)
                .build())
                .when(productRepository)
                .findByCode("123");
        addReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenValidReceiptThenOneReceiptShouldBePersisted() throws Exception {
        List<ReceiptEntity> receiptEntities = new ArrayList<>();
        Mockito.doReturn(new ProductEntityBuilder().setQuantityRemaining(1L).setQuantitySold(0L).build())
                .when(productRepository)
                .findByCode("123");
        Mockito.doAnswer(invocationOnMock -> receiptEntities.add((ReceiptEntity) invocationOnMock.getArguments()[0]))
                .when(receiptRepository)
                .save(Matchers.<ReceiptEntity>any());
        addReceiptUseCase.execute(receipt);
        assertEquals(1, receiptEntities.size());
    }

    @Test
    public void GivenValidReceiptThenProductQuantityRemainingAndQuantitySoldShouldBeUpdatedCorrectly() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doReturn(new ProductEntityBuilder().setQuantityRemaining(1L).setQuantitySold(0L).build())
                .when(productRepository)
                .findByCode("123");
        Mockito.doAnswer(invocationOnMock -> productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]))
                .when(productRepository)
                .save(Matchers.<ProductEntity>any());
        addReceiptUseCase.execute(receipt);
        ProductEntity expectedProductEntity = new ProductEntityBuilder().setQuantityRemaining(0L).setQuantitySold(1L).build();
        assertEquals(expectedProductEntity, productEntities.get(0));
    }

    private Receipt getValidReceipt() {
        return new ReceiptBuilder()
                .setProduct(new ProductBuilder().setCode("123").build())
                .setDate("01-01-2017 00:00:00")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantity(1L)
                .setTotal(BigDecimal.valueOf(1.5))
                .build();
    }

    private void expectUseCaseException(String expectedMessage) {
        expectedException.expect(UseCaseException.class);
        expectedException.expectMessage(expectedMessage);
    }
}
