package interactors;

import adapters.UseCase;
import beans.Product;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;
import persistence.ReceiptRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by u624 on 3/25/17.
 */
public class DeleteProductUseCase implements UseCase<Product> {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    private Map<Predicate<ProductEntity>, String> repositoryValidationsMessagesMap = new HashMap<>();

    public DeleteProductUseCase() {
        repositoryValidationsMessagesMap.put(Objects::nonNull,
                "Product doesn't exit");
        repositoryValidationsMessagesMap.put(p -> receiptRepository.findByProductEntity(p).isEmpty(),
                "There are receipts that depend on this product");
    }

    @Override
    public void execute(Product product) throws UseCaseException {
        ProductEntity productEntity = productRepository.findByCode(product.getCode());
        doRepositoryValidations(productEntity);
        productRepository.delete(productEntity);
    }

    private void doRepositoryValidations(ProductEntity productEntity) throws UseCaseException {
        Optional<Map.Entry<Predicate<ProductEntity>, String>> entry =
                repositoryValidationsMessagesMap.entrySet()
                        .stream()
                        .filter(e -> !e.getKey().test(productEntity))
                        .findFirst();
        if (entry.isPresent()) {
            throw new UseCaseException(entry.get().getValue());
        }
    }
}
