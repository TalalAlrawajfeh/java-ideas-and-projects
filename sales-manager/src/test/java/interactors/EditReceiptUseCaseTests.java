package interactors;

import beans.Receipt;
import beans.builders.ProductBuilder;
import beans.builders.ReceiptBuilder;
import entities.ProductEntity;
import entities.ReceiptEntity;
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
import java.util.Optional;

import static junit.framework.Assert.assertEquals;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class EditReceiptUseCaseTests {
    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private EditReceiptUseCase editReceiptUseCase = new EditReceiptUseCase();

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
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithProductWithNoCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        receipt.getProduct().setCode(null);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithProductWithInvalidCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The code field is not valid");
        receipt.getProduct().setCode("1 23");
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithNoPriceThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The price field is not valid");
        receipt.setPrice(null);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithNoQuantityThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("The quantity field is not valid");
        receipt.setQuantity(null);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithProductThatDoesNotExistThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Product doesn't exist");
        Mockito.doReturn(null).when(productRepository).findByCode("123");
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithNoIdThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Invalid ID or receipt doesn't exist");
        Mockito.doReturn(new ProductEntity()).when(productRepository).findByCode("123");
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenReceiptWithIdThatDoesNotExistThenUseCaseExceptionShouldBeThrown() throws Exception {
        expectUseCaseException("Invalid ID or receipt doesn't exist");
        Mockito.doReturn(new ProductEntity()).when(productRepository).findByCode("123");
        Mockito.doReturn(null).when(receiptRepository).findById(1L);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenNewReceiptWithProductSameAsOldReceiptProductAndWithQuantityExceedingTheProductQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws UseCaseException {
        expectUseCaseException("Product quantity remaining is not sufficient");
        mockRepositoriesReturns();
        receipt.setQuantity(3L);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenNewValidReceiptWithProductSameAsOldReceiptProductAndWithQuantityEqualsTheProductQuantityRemainingThenNoExceptionShouldBeThrown() throws UseCaseException {
        mockRepositoriesReturns();
        receipt.setQuantity(2L);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenNewReceiptWithProductDifferentThanOldReceiptProductAndWithQuantityExceedingTheProductQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws UseCaseException {
        expectUseCaseException("Product quantity remaining is not sufficient");
        mockRepositoriesReturns();
        Mockito.doReturn(new ProductEntityBuilder()
                .setCode("321")
                .setQuantitySold(1L)
                .setQuantityRemaining(2L)
                .build())
                .when(productRepository)
                .findByCode("321");
        receipt.setProduct(new ProductBuilder().setCode("321").build());
        receipt.setQuantity(3L);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenNewValidReceiptWithProductDifferentThanOldReceiptProductAndWithQuantityEqualsTheProductQuantityRemainingThenNoExceptionShouldBeThrown() throws UseCaseException {
        mockRepositoriesReturns();
        Mockito.doReturn(new ProductEntityBuilder()
                .setCode("321")
                .setQuantitySold(1L)
                .setQuantityRemaining(2L)
                .build())
                .when(productRepository)
                .findByCode("321");
        receipt.setProduct(new ProductBuilder().setCode("321").build());
        receipt.setQuantity(2L);
        editReceiptUseCase.execute(receipt);
    }

    @Test
    public void GivenValidReceiptThenOneReceiptEntityShouldBePersisted() throws Exception {
        List<ReceiptEntity> receiptEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> receiptEntities.add((ReceiptEntity) invocationOnMock.getArguments()[0]))
                .when(receiptRepository)
                .save(Matchers.<ReceiptEntity>any());
        mockRepositoriesReturns();
        editReceiptUseCase.execute(receipt);
        assertEquals(1, receiptEntities.size());
        assertEquals(receipt.convert(), receiptEntities.get(0));
    }

    @Test
    public void GivenNewValidReceiptWithProductSameAsOldReceiptProductAndWithQuantityEqualsTheProductQuantityRemainingThenProductShouldBeUpdatedCorrectly() throws UseCaseException {
        List<ProductEntity> productEntities = new ArrayList<>();
        mockProductRepositorySave(productEntities);
        mockRepositoriesReturns();
        receipt.setQuantity(2L);
        editReceiptUseCase.execute(receipt);
        assertEquals(1, productEntities.size());
        ProductEntity productEntity = productEntities.get(0);
        assertEquals(0, (long) productEntity.getQuantityRemaining());
        assertEquals(2, (long) productEntity.getQuantitySold());
    }

    @Test
    public void GivenNewValidReceiptWithProductDifferentThanOldReceiptProductAndWithQuantityEqualsTheProductQuantityRemainingThenTheTwoProductsShouldBeUpdatedCorrectly() throws UseCaseException {
        List<ProductEntity> productEntities = new ArrayList<>();
        mockProductRepositorySave(productEntities);
        mockRepositoriesReturns();
        Mockito.doReturn(new ProductEntityBuilder()
                .setCode("321")
                .setQuantitySold(1L)
                .setQuantityRemaining(2L)
                .build())
                .when(productRepository)
                .findByCode("321");
        receipt.setProduct(new ProductBuilder().setCode("321").build());
        receipt.setQuantity(2L);
        editReceiptUseCase.execute(receipt);
        assertEquals(2, productEntities.size());
        ProductEntity oldProductEntity = productEntities.get(0);
        assertEquals(2, (long) oldProductEntity.getQuantityRemaining());
        assertEquals(0, (long) oldProductEntity.getQuantitySold());
        ProductEntity newProductEntity = productEntities.get(1);
        assertEquals(0, (long) newProductEntity.getQuantityRemaining());
        assertEquals(3, (long) newProductEntity.getQuantitySold());
    }

    private void mockProductRepositorySave(List<ProductEntity> productEntities) {
        Mockito.doAnswer(invocationOnMock -> {
            ProductEntity productEntity = (ProductEntity) invocationOnMock.getArguments()[0];
            Optional<ProductEntity> productEntityOptional = productEntities
                    .stream()
                    .filter(p -> productEntity.getCode().equals(p.getCode()))
                    .findAny();
            if (productEntityOptional.isPresent()) {
                productEntities.remove(productEntityOptional.get());
            }
            productEntities.add(productEntity);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
    }

    private void mockRepositoriesReturns() {
        ProductEntity productEntity = new ProductEntityBuilder()
                .setCode("123")
                .setQuantityRemaining(1L)
                .setQuantitySold(1L)
                .build();
        Mockito.doReturn(productEntity)
                .when(productRepository)
                .findByCode("123");
        Mockito.doReturn(new ReceiptEntityBuilder()
                .setProductEntity(productEntity)
                .setQuantity(1L)
                .build())
                .when(receiptRepository)
                .findById(1L);
    }

    private Receipt getValidReceipt() {
        return new ReceiptBuilder()
                .setId(1L)
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
