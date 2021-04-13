package interactors;

import adapters.UseCase;
import beans.Product;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * Created by u624 on 3/25/17.
 */
public class ListAllProductsUseCase implements UseCase<List<Product>> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void execute(List<Product> products) throws UseCaseException {
        if (Objects.isNull(products)) {
            throw new UseCaseException("Products parameter cannot be null");
        }
        StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .forEach(p -> products.add(p.convert()));
    }
}
