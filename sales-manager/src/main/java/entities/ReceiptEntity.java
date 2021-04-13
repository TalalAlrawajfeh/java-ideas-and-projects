package entities;

import adapters.Convertable;
import beans.Receipt;
import beans.builders.ReceiptBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by u624 on 3/22/17.
 */
@Entity
@Table(name = "receipts")
public class ReceiptEntity implements Serializable, Convertable<Receipt> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RECEIPT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT")
    private ProductEntity productEntity;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal price;

    @Column(name = "PRODUCT_QUANTITY")
    private Long quantity;

    @Column(name = "RECEIPT_TOTAL")
    private BigDecimal total;

    @Column(name = "RECEIPT_DATE")
    private String date;

    public ReceiptEntity() {
        /* default constructor */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
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
    public Receipt convert() {
        return new ReceiptBuilder()
                .setId(id)
                .setProduct(Objects.isNull(productEntity) ? null : productEntity.convert())
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
        ReceiptEntity that = (ReceiptEntity) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (productEntity != null ? !productEntity.equals(that.productEntity) : that.productEntity != null) {
            return false;
        }
        if (price != null ? !price.equals(that.price) : that.price != null) {
            return false;
        }
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) {
            return false;
        }
        if (total != null ? !total.equals(that.total) : that.total != null) {
            return false;
        }
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (productEntity != null ? productEntity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
