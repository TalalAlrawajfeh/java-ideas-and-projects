package persistence;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entities.SiteSettingsEntity;

@Repository
public interface SiteSettingsRepository extends CrudRepository<SiteSettingsEntity, Serializable> {
	SiteSettingsEntity findByDeliveryUrl(String deliveryUrl);
}
