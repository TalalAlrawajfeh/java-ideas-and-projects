package interactors;

import beans.Receipt;
import beans.builders.ProductBuilder;
import beans.builders.ReceiptBuilder;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;
import entities.builders.ReceiptEntityBuilder;
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
public class DeleteReceiptsUseCaseTests {
    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    public DeleteReceiptUseCase deleteReceiptUseCase = new DeleteReceiptUseCase();

    private Receipt receipt;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        receipt = getValidReceipt();
    }

    @Test
    public void GivenReceiptThatDoesNotExistThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Receipt doesn't exist");
        Mockito.doReturn(null)
                .when(receiptRepository)
                .findById(1L);
        deleteReceiptUseCase.execute(receipt);
    }

    @Test
    public void WhenReceiptIsDeletedThenProductQuantityRemainingAndQuantitySoldShouldBeUpdated() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doReturn(new ReceiptEntityBuilder()
                .setProductEntity(new ProductEntityBuilder()
                        .setQuantityRemaining(0L)
                        .setQuantitySold(1L)
                        .build())
                .setQuantity(1L)
                .build())
                .when(receiptRepository)
                .findById(1L);
        Mockito.doAnswer(invocationOnMock -> productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]))
                .when(productRepository)
                .save(Matchers.<ProductEntity>any());
        deleteReceiptUseCase.execute(receipt);
        assertEquals(1, productEntities.size());
        assertEquals(new ProductEntityBuilder().setQuantityRemaining(1L).setQuantitySold(0L).build(),
                productEntities.get(0));
    }

    private Receipt getValidReceipt() {
        return new ReceiptBuilder()
                .setId(1L)
                .setProduct(new ProductBuilder().setCode("123").build())
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
