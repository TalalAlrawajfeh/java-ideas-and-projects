package entities.builders;

import adapters.BeanBuilder;
import entities.ProductEntity;
import entities.ReceiptEntity;

import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
public class ReceiptEntityBuilder implements BeanBuilder<ReceiptEntity> {
    private ReceiptEntity receiptEntity = new ReceiptEntity();

    public ReceiptEntityBuilder setId(Long id) {
        receiptEntity.setId(id);
        return this;
    }

    public ReceiptEntityBuilder setProductEntity(ProductEntity productEntity) {
        receiptEntity.setProductEntity(productEntity);
        return this;
    }

    public ReceiptEntityBuilder setPrice(BigDecimal price) {
        receiptEntity.setPrice(price);
        return this;
    }

    public ReceiptEntityBuilder setQuantity(Long quantity) {
        receiptEntity.setQuantity(quantity);
        return this;
    }

    public ReceiptEntityBuilder setTotal(BigDecimal total) {
        receiptEntity.setTotal(total);
        return this;
    }

    public ReceiptEntityBuilder setDate(String date) {
        receiptEntity.setDate(date);
        return this;
    }

    @Override
    public ReceiptEntity build() {
        return receiptEntity;
    }
}
