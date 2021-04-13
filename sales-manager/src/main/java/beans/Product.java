package beans;

import adapters.Convertable;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
public class Product implements Serializable, Convertable<ProductEntity> {
    private String code;
    private String description;
    private BigDecimal price;
    private Long quantityRemaining;
    private Long quantitySold;

    public Product() {
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
    public ProductEntity convert() {
        return new ProductEntityBuilder()
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
        Product product = (Product) o;
        if (code != null ? !code.equals(product.code) : product.code != null) {
            return false;
        }
        if (description != null ? !description.equals(product.description) : product.description != null) {
            return false;
        }
        if (price != null ? !price.equals(product.price) : product.price != null) {
            return false;
        }
        if (quantityRemaining != null ? !quantityRemaining.equals(product.quantityRemaining) : product.quantityRemaining != null) {
            return false;
        }
        return quantitySold != null ? quantitySold.equals(product.quantitySold) : product.quantitySold == null;
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
