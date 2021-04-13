package interactors;

import adapters.UseCase;
import beans.Product;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by u624 on 3/25/17.
 */
public class AddProductUseCase implements UseCase<Product> {
    @Autowired
    private ProductRepository productRepository;

    private Map<Predicate<Product>, String> productsValidationsMessagesMap = new HashMap<>();

    public AddProductUseCase() {
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getCode())
                        && p.getCode().matches("[A-Z0-9]+"),
                "The code field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getDescription())
                        && p.getDescription().matches(".*[\\w]+.*"),
                "The description field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getPrice()),
                "The price field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getQuantityRemaining()),
                "The quantity field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.isNull(productRepository.findByCode(p.getCode())),
                "Another product with the same code already exists");
    }

    @Override
    public void execute(Product product) throws UseCaseException {
        prepareAndValidateProduct(product);
        productRepository.save(product.convert());
    }

    private void prepareAndValidateProduct(Product product) throws UseCaseException {
        product.setCode(product.getCode().toUpperCase());
        product.setQuantitySold(0L);
        validateProduct(product);
    }

    private void validateProduct(Product product) throws UseCaseException {
        Optional<Map.Entry<Predicate<Product>, String>> validationPair = productsValidationsMessagesMap.entrySet()
                .stream()
                .filter(e -> !e.getKey().test(product))
                .findFirst();
        if (validationPair.isPresent()) {
            throw new UseCaseException(validationPair.get().getValue());
        }
    }
}
