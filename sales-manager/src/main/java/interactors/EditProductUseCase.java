package interactors;

import adapters.UseCase;
import beans.Pair;
import beans.Product;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;
import persistence.ReceiptRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by u624 on 3/26/17.
 */
public class EditProductUseCase implements UseCase<Pair<String, Product>> {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    private ProductEntity productEntity;
    private List<Pair<Predicate<Product>, String>> productValidations = new ArrayList<>();
    private List<Pair<Predicate<Pair<String, Product>>, String>> repositoryValidations = new ArrayList<>();

    public EditProductUseCase() {
        initializeValidations();
    }

    @Override
    public void execute(Pair<String, Product> oldCodeProductPair) throws UseCaseException {
        Product product = oldCodeProductPair.getSecond();
        prepareAndValidateProduct(oldCodeProductPair, product);
        if (!oldCodeProductPair.getFirst().equals(oldCodeProductPair.getSecond().getCode())) {
            deleteOldProductEntity();
        }
        productRepository.save(product.convert());
    }

    private void deleteOldProductEntity() throws UseCaseException {
        productRepository.delete(productEntity);
    }

    private void prepareAndValidateProduct(Pair<String, Product> oldCodeProductPair, Product product)
            throws UseCaseException {
        product.setCode(product.getCode().toUpperCase());
        validateProduct(product);
        productEntity = productRepository.findByCode(oldCodeProductPair.getFirst());
        doRepositoryValidations(oldCodeProductPair);
        product.setQuantitySold(productEntity.getQuantitySold());
    }

    private void doRepositoryValidations(Pair<String, Product> oldCodeProductPair) throws UseCaseException {
        Optional<Pair<Predicate<Pair<String, Product>>, String>> validationPair = repositoryValidations
                .stream()
                .sequential()
                .filter(e -> !e.getFirst().test(oldCodeProductPair))
                .findFirst();
        if (validationPair.isPresent()) {
            throw new UseCaseException(validationPair.get().getSecond());
        }
    }

    private void validateProduct(Product product) throws UseCaseException {
        Optional<Pair<Predicate<Product>, String>> validationPair = productValidations
                .stream()
                .sequential()
                .filter(e -> !e.getFirst().test(product))
                .findFirst();
        if (validationPair.isPresent()) {
            throw new UseCaseException(validationPair.get().getSecond());
        }
    }

    private void initializeValidations() {
        productValidations.add(new Pair<>(p -> Objects.nonNull(p.getCode())
                && p.getCode().matches("[A-Z0-9]+"),
                "The code field is not valid"));
        productValidations.add(new Pair<>(p -> Objects.nonNull(p.getDescription())
                && p.getDescription().matches(".*\\w+.*"),
                "The description field is not valid"));
        productValidations.add(new Pair<>(p -> Objects.nonNull(p.getPrice()),
                "The price field is not valid"));
        productValidations.add(new Pair<>(p -> Objects.nonNull(p.getQuantityRemaining()),
                "The quantity field is not valid"));
        repositoryValidations.add(new Pair<>(p -> Objects.nonNull(productEntity),
                "Product doesn't exist"));
        repositoryValidations.add(new Pair<>(p -> p.getFirst().equals(p.getSecond().getCode())
                || receiptRepository.findByProductEntity(productEntity).isEmpty(),
                "There are receipts that depend on this product"));
        repositoryValidations.add(new Pair<>(p -> p.getFirst().equals(p.getSecond().getCode())
                || Objects.isNull(productRepository.findByCode(p.getSecond().getCode())),
                "Another product with the same code already exists"));
    }
}
