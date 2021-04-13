package entities.builders;

import adapters.BeanBuilder;
import entities.ProductEntity;

import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
public class ProductEntityBuilder implements BeanBuilder<ProductEntity> {
    private ProductEntity productEntity = new ProductEntity();

    public ProductEntityBuilder setCode(String code) {
        productEntity.setCode(code);
        return this;
    }

    public ProductEntityBuilder setDescription(String description) {
        productEntity.setDescription(description);
        return this;
    }

    public ProductEntityBuilder setPrice(BigDecimal price) {
        productEntity.setPrice(price);
        return this;
    }

    public ProductEntityBuilder setQuantityRemaining(Long quantityRemaining) {
        productEntity.setQuantityRemaining(quantityRemaining);
        return this;
    }

    public ProductEntityBuilder setQuantitySold(Long quantitySold) {
        productEntity.setQuantitySold(quantitySold);
        return this;
    }

    @Override
    public ProductEntity build() {
        return productEntity;
    }
}
