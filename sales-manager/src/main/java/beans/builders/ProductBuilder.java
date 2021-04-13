package beans.builders;

import adapters.BeanBuilder;
import beans.Product;

import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
public class ProductBuilder implements BeanBuilder<Product> {
    private Product product = new Product();

    public ProductBuilder setCode(String code) {
        product.setCode(code);
        return this;
    }

    public ProductBuilder setDescription(String description) {
        product.setDescription(description);
        return this;
    }

    public ProductBuilder setPrice(BigDecimal price) {
        product.setPrice(price);
        return this;
    }

    public ProductBuilder setQuantityRemaining(Long quantityRemaining) {
        product.setQuantityRemaining(quantityRemaining);
        return this;
    }

    public ProductBuilder setQuantitySold(Long quantitySold) {
        product.setQuantitySold(quantitySold);
        return this;
    }

    @Override
    public Product build() {
        return product;
    }
}
