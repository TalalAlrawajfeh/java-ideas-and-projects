package entities;

import adapters.Convertable;
import beans.Product;
import beans.builders.ProductBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
@Entity
@Table(name = "products")
public class ProductEntity implements Serializable, Convertable<Product> {
    @Id
    @Column(name = "PRODUCT_CODE")
    private String code;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String description;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal price;

    @Column(name = "QUANTITY_REMAINING")
    private Long quantityRemaining;

    @Column(name = "QUANTITY_SOLD")
    private Long quantitySold;

    public ProductEntity() {
        /* default constructor */
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(Long quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public Product convert() {
        return new ProductBuilder()
                .setCode(code)
                .setDescription(description)
                .setPrice(price)
                .setQuantityRemaining(quantityRemaining)
                .setQuantitySold(quantitySold)
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
        ProductEntity that = (ProductEntity) o;
        if (code != null ? !code.equals(that.code) : that.code != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (price != null ? !price.equals(that.price) : that.price != null) {
            return false;
        }
        if (quantityRemaining != null ? !quantityRemaining.equals(that.quantityRemaining) : that.quantityRemaining != null) {
            return false;
        }
        return quantitySold != null ? quantitySold.equals(that.quantitySold) : that.quantitySold == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (quantityRemaining != null ? quantityRemaining.hashCode() : 0);
        result = 31 * result + (quantitySold != null ? quantitySold.hashCode() : 0);
        return result;
    }
}
