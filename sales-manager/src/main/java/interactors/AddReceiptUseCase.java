package interactors;

import adapters.UseCase;
import beans.Pair;
import beans.Receipt;
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
 * Created by u624 on 3/25/17.
 */
public class AddReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<Pair<Predicate<Receipt>, String>> validations = new ArrayList<>();

    public AddReceiptUseCase() {
        validations.add(new Pair<>(r -> Objects.nonNull(r.getProduct())
                && Objects.nonNull(r.getProduct().getCode())
                && r.getProduct().getCode().matches("[A-Z0-9]+"),
                "The code field is not valid"));
        validations.add(new Pair<>(r -> Objects.nonNull(r.getPrice()),
                "The price field is not valid"));
        validations.add(new Pair<>(r -> Objects.nonNull(r.getQuantity()),
                "The quantity field is not valid"));
        validations.add(new Pair<>(r -> Objects.nonNull(productRepository.findByCode(r.getProduct().getCode())),
                "Product doesn't exist"));
        validations.add(new Pair<>(r -> productRepository.findByCode(r.getProduct().getCode()).getQuantityRemaining()
                >= r.getQuantity(),
                "Product quantity remaining is not sufficient"));
    }

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        validateReceipt(receipt);
        receiptRepository.save(receipt.convert());
        updateProduct(receipt);
    }

    private void updateProduct(Receipt receipt) {
        ProductEntity productEntity = productRepository.findByCode(receipt.getProduct().getCode());
        Long quantity = receipt.getQuantity();
        productEntity.setQuantityRemaining(productEntity.getQuantityRemaining() - quantity);
        productEntity.setQuantitySold(productEntity.getQuantitySold() + quantity);
        productRepository.save(productEntity);
    }

    private void validateReceipt(Receipt receipt) throws UseCaseException {
        Optional<Pair<Predicate<Receipt>, String>> validationPair = validations
                .stream()
                .sequential()
                .filter(p -> !p.getFirst().test(receipt))
                .findFirst();
        if (validationPair.isPresent()) {
            throw new UseCaseException(validationPair.get().getSecond());
        }
    }
}
