package persistence;

import entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * Created by u624 on 3/24/17.
 */
public interface UserRepository extends CrudRepository<UserEntity, Serializable> {
    UserEntity findByUsername(String username);
}
