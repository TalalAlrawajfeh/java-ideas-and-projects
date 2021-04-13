package persistence.jpa;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import persistence.EntityPersistenceProvider;
import persistence.jpa.exceptions.JPAEntityPersistenceProviderException;

public class JPAEntityPersistenceProvider implements EntityPersistenceProvider<EntityManager> {
	private static final String JPA_ENTITY_PERSISTENCE_PROVIDER_TERMINATED = "JPAEntityPersistenceProvider was terminated";
	private static final String JPA_ENTITY_PERSISTENCE_PROVIDER_NOT_INITIATED = "JPAEntityPersistenceProvider was not initiated";
	private static final String PERSISTENCE_UNIT_NAME = "jpa.persistence";
	private EntityManager entityManager;

	@Override
	public void initiateProvider() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}

	@Override
	public void commitChanges() {
		checkEntityManager();
		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.getTransaction().begin();
	}

	private void checkEntityManager() {
		if (Objects.isNull(entityManager))
			throw new JPAEntityPersistenceProviderException(JPA_ENTITY_PERSISTENCE_PROVIDER_NOT_INITIATED);

		if (!entityManager.isOpen())
			throw new JPAEntityPersistenceProviderException(JPA_ENTITY_PERSISTENCE_PROVIDER_TERMINATED);
	}

	@Override
	public void discardChanges() {
		checkEntityManager();

		entityManager.getTransaction().rollback();
		entityManager.getTransaction().begin();
	}

	@Override
	public void terminateProvider() {
		checkEntityManager();

		entityManager.close();
	}

	@Override
	public EntityManager getPersistenceManager() {
		checkEntityManager();

		return entityManager;
	}
}
