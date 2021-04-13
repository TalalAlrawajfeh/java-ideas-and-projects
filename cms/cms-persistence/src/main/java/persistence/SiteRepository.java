package persistence;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entities.SiteEntity;

@Repository
public interface SiteRepository extends CrudRepository<SiteEntity, Serializable> {
	SiteEntity findByUri(String uri);
}
