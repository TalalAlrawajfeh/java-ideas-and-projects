package beans.builders;

import adapters.BeanBuilder;
import beans.Product;
import beans.Receipt;

import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
public class ReceiptBuilder implements BeanBuilder<Receipt> {
    private Receipt receipt = new Receipt();

    public ReceiptBuilder setId(Long id) {
        receipt.setId(id);
        return this;
    }

    public ReceiptBuilder setProduct(Product product) {
        receipt.setProduct(product);
        return this;
    }

    public ReceiptBuilder setPrice(BigDecimal price) {
        receipt.setPrice(price);
        return this;
    }

    public ReceiptBuilder setQuantity(Long quantity) {
        receipt.setQuantity(quantity);
        return this;
    }

    public ReceiptBuilder setTotal(BigDecimal total) {
        receipt.setTotal(total);
        return this;
    }

    public ReceiptBuilder setDate(String date) {
        receipt.setDate(date);
        return this;
    }

    @Override
    public Receipt build() {
        return receipt;
    }
}
