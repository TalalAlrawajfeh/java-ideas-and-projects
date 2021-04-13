package interactors;

import adapters.UseCase;
import beans.Receipt;
import entities.ProductEntity;
import entities.ReceiptEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;
import persistence.ReceiptRepository;

import java.util.Objects;

/**
 * Created by u624 on 3/25/17.
 */
public class DeleteReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        ReceiptEntity receiptEntity = receiptRepository.findById(receipt.getId());
        ensureReceiptExist(receiptEntity);
        updateProduct(receiptEntity);
        receiptRepository.delete(receipt.convert());
    }

    private void ensureReceiptExist(ReceiptEntity receiptEntity) throws UseCaseException {
        if (Objects.isNull(receiptEntity)) {
            throw new UseCaseException("Receipt doesn't exist");
        }
    }

    private void updateProduct(ReceiptEntity receiptEntity) {
        ProductEntity productEntity = receiptEntity.getProductEntity();
        Long quantity = receiptEntity.getQuantity();
        productEntity.setQuantitySold(productEntity.getQuantitySold() - quantity);
        productEntity.setQuantityRemaining(productEntity.getQuantityRemaining() + quantity);
        productRepository.save(productEntity);
    }
}
