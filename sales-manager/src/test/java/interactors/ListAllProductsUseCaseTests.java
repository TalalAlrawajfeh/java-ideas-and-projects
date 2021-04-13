package interactors;

import beans.Product;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ProductRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListAllProductsUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ListAllProductsUseCase listAllProductsUseCase = new ListAllProductsUseCase();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void GivenNullProductsListThenUseCaseExceptionShouldBeThrown() throws UseCaseException {
        expectUseCaseException("Products parameter cannot be null");
        listAllProductsUseCase.execute(null);
    }

    @Test
    public void GivenProductsListThenAllProductEntitiesShouldBeAddedToIt() throws UseCaseException {
        List<Product> products = new ArrayList<>();
        ProductEntity firstProductEntity = new ProductEntityBuilder().setCode("123").build();
        ProductEntity secondProductEntity = new ProductEntityBuilder().setCode("321").build();
        List<ProductEntity> productEntities = Arrays.asList(firstProductEntity,
                secondProductEntity);
        Mockito.doReturn(productEntities).when(productRepository).findAll();
        listAllProductsUseCase.execute(products);
        assertEquals(2, products.size());
        assertTrue(products.contains(firstProductEntity.convert()));
        assertTrue(products.contains(secondProductEntity.convert()));
    }

    private void expectUseCaseException(String expectedMessage) {
        expectedException.expect(UseCaseException.class);
        expectedException.expectMessage(expectedMessage);
    }
}
