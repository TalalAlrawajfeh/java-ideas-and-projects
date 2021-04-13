package persistence;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entities.PageEntity;
import entities.PublishedPageEntity;

@Repository
public interface PublishedPageRepository extends CrudRepository<PublishedPageEntity, Serializable> {
	PublishedPageEntity findByUri(String uri);
	
	PublishedPageEntity findByCorrespondingPage(PageEntity correspondingPage);
}
