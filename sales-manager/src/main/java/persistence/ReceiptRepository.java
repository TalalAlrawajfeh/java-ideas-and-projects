package persistence;

import entities.ProductEntity;
import entities.ReceiptEntity;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by u624 on 3/22/17.
 */
public interface ReceiptRepository extends CrudRepository<ReceiptEntity, Serializable> {
    List<ReceiptEntity> findByProductEntity(ProductEntity productEntity);

    ReceiptEntity findById(Long id);
}
