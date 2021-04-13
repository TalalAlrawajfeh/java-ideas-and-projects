package persistence;

import entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * Created by u624 on 3/22/17.
 */
public interface ProductRepository extends CrudRepository<ProductEntity, Serializable> {
    ProductEntity findByCode(String code);
}
