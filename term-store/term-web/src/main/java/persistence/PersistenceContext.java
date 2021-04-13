package persistence;

import javax.persistence.EntityManager;

import persistence.beans.Category;
import persistence.beans.Term;
import persistence.jpa.JPAEntityPersistenceProcessor;
import persistence.jpa.JPAEntityPersistenceProvider;
import persistence.jpa.entities.CategoryEntity;
import persistence.jpa.entities.TermEntity;

public class PersistenceContext {
	private static boolean created = false;
	private static PersistenceContext persistenceContext;

	private EntityPersistenceProvider<EntityManager> persistenceProvider;
	private EntityPersistenceProcessor<Category, CategoryEntity> categoryPersistenceProcessor;
	private EntityPersistenceProcessor<Term, TermEntity> termPersistenceProcessor;

	public PersistenceContext() {
		if (!created) {
			JPAEntityPersistenceProvider jpaPersistenceProvider = new JPAEntityPersistenceProvider();
			jpaPersistenceProvider.initiateProvider();

			categoryPersistenceProcessor = new JPAEntityPersistenceProcessor<>(jpaPersistenceProvider);
			termPersistenceProcessor = new JPAEntityPersistenceProcessor<>(jpaPersistenceProvider);
			persistenceProvider = jpaPersistenceProvider;
			persistenceContext = this;
			created = true;
		} else {
			persistenceProvider = persistenceContext.getPersistenceProvider();
			categoryPersistenceProcessor = persistenceContext.getCategoryPersistenceProcessor();
			termPersistenceProcessor = persistenceContext.getTermPersistenceProcessor();
		}
	}

	public EntityPersistenceProvider<EntityManager> getPersistenceProvider() {
		return persistenceProvider;
	}

	public EntityPersistenceProcessor<Category, CategoryEntity> getCategoryPersistenceProcessor() {
		return categoryPersistenceProcessor;
	}

	public EntityPersistenceProcessor<Term, TermEntity> getTermPersistenceProcessor() {
		return termPersistenceProcessor;
	}
}
