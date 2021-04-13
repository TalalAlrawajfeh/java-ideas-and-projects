package beans;

import adapters.Convertable;
import entities.ReceiptEntity;
import entities.builders.ReceiptEntityBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by u624 on 3/22/17.
 */
public class Receipt implements Serializable, Convertable<ReceiptEntity> {
    private Long id;
    private Product product;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal total;
    private String date;

    public Receipt() {
        /* default constructor */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public ReceiptEntity convert() {
        return new ReceiptEntityBuilder()
                .setId(id)
                .setProductEntity(Objects.isNull(product) ? null : product.convert())
                .setPrice(price)
                .setQuantity(quantity)
                .setTotal(total)
                .setDate(date)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Receipt receipt = (Receipt) o;
        if (id != null ? !id.equals(receipt.id) : receipt.id != null) {
            return false;
        }
        if (product != null ? !product.equals(receipt.product) : receipt.product != null) {
            return false;
        }
        if (price != null ? !price.equals(receipt.price) : receipt.price != null) {
            return false;
        }
        if (quantity != null ? !quantity.equals(receipt.quantity) : receipt.quantity != null) {
            return false;
        }
        if (total != null ? !total.equals(receipt.total) : receipt.total != null) {
            return false;
        }
        return date != null ? date.equals(receipt.date) : receipt.date == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
