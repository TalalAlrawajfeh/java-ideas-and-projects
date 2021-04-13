package interactors;

import beans.builders.ProductBuilder;
import entities.ProductEntity;
import entities.ReceiptEntity;
import entities.builders.ProductEntityBuilder;
import exceptions.UseCaseException;
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
 * Created by u624 on 4/1/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteProductUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private DeleteProductUseCase deleteProductUseCase = new DeleteProductUseCase();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void GivenProductThatDoesNotExistThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Product doesn't exit");
        Mockito.doReturn(null).when(productRepository).findByCode("123");
        deleteProductUseCase.execute(new ProductBuilder().setCode("123").build());
    }

    @Test
    public void GivenProductWithReceiptChildThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("There are receipts that depend on this product");
        ProductEntity productEntity = getProductEntity();
        Mockito.doReturn(productEntity).when(productRepository).findByCode("123");
        Mockito.doReturn(Arrays.asList(new ReceiptEntity())).when(receiptRepository).findByProductEntity(productEntity);
        deleteProductUseCase.execute(productEntity.convert());
    }

    @Test
    public void GivenValidProductThenOneProductEntityShouldBeDeleted() throws Exception {
        ProductEntity productEntity = getProductEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(productEntity);
        Mockito.doReturn(productEntity).when(productRepository).findByCode("123");
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.remove(invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).delete(Matchers.<ProductEntity>any());
        deleteProductUseCase.execute(productEntity.convert());
        assertEquals(0, productEntities.size());
    }

    private ProductEntity getProductEntity() {
        return new ProductEntityBuilder()
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
