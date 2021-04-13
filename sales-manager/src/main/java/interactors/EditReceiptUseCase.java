package interactors;

import adapters.UseCase;
import beans.Pair;
import beans.Receipt;
import entities.ProductEntity;
import entities.ReceiptEntity;
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
 * Created by u624 on 3/31/17.
 */
public class EditReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<Pair<Predicate<Receipt>, String>> validations = new ArrayList<>();

    private ReceiptEntity oldReceiptEntity;
    private ProductEntity oldProductEntity;
    private ProductEntity newProductEntity;

    private Predicate<Receipt> isnNewProductQuantityRemainingSufficient = r -> {
        oldReceiptEntity = receiptRepository.findById(r.getId());
        oldProductEntity = productRepository.findByCode(oldReceiptEntity.getProductEntity().getCode());
        newProductEntity = productRepository.findByCode(r.getProduct().getCode());
        if (oldProductEntity.getCode().equals(newProductEntity.getCode())) {
            return oldProductEntity.getQuantityRemaining() + oldReceiptEntity.getQuantity() >= r.getQuantity();
        }
        return newProductEntity.getQuantityRemaining() >= r.getQuantity();
    };

    public EditReceiptUseCase() {
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
        validations.add(new Pair<>(r -> Objects.nonNull(r.getId())
                && Objects.nonNull(receiptRepository.findById(r.getId())),
                "Invalid ID or receipt doesn't exist"));
        validations.add(new Pair<>(isnNewProductQuantityRemainingSufficient,
                "Product quantity remaining is not sufficient"));
    }

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        validateReceipt(receipt);
        receipt.setDate(oldReceiptEntity.getDate());
        receiptRepository.save(receipt.convert());
        updateProducts(receipt);
    }

    private void updateProducts(Receipt newReceipt) {
        updateOldProduct();
        updateNewProduct(newReceipt);
    }

    private void updateNewProduct(Receipt newReceipt) {
        Long newQuantity = newReceipt.getQuantity();
        if (newProductEntity.getCode().equals(oldProductEntity.getCode())) {
            newProductEntity = oldProductEntity;
        }
        newProductEntity.setQuantityRemaining(newProductEntity.getQuantityRemaining() - newQuantity);
        newProductEntity.setQuantitySold(newProductEntity.getQuantitySold() + newQuantity);
        productRepository.save(newProductEntity);
    }

    private void updateOldProduct() {
        Long oldQuantity = oldReceiptEntity.getQuantity();
        oldProductEntity.setQuantityRemaining(oldProductEntity.getQuantityRemaining() + oldQuantity);
        oldProductEntity.setQuantitySold(oldProductEntity.getQuantitySold() - oldQuantity);
        productRepository.save(oldProductEntity);
    }

    private void validateReceipt(Receipt receipt) throws UseCaseException {
        Optional<Pair<Predicate<Receipt>, String>> entry = validations
                .stream()
                .sequential()
                .filter(p -> !p.getFirst().test(receipt))
                .findFirst();
        if (entry.isPresent()) {
            throw new UseCaseException(entry.get().getSecond());
        }
    }
}
