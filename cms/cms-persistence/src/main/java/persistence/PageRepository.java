package persistence;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entities.PageEntity;

@Repository
public interface PageRepository extends CrudRepository<PageEntity, Serializable> {
	PageEntity findByUri(String uri);
}
