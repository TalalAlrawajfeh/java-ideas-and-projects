package persistence;

import java.util.List;

public interface EntityPersistenceProcessor<J, T extends PersistenceEntity<J>> {
	T findById(final Class<T> entityClass, final Long id);

	List<T> findByFields(final Class<T> entityClass, final List<EntityPersistenceField<?>> fields);

	List<T> findAll(final Class<T> entityClass, final int maxResults);

	void save(final T entity);

	void update(final T entity);

	void delete(final T entity);

	void deleteById(final Class<T> entityClass, final Long id);
	
	void lock(final T entity, LockMode lockMode);
	
	void unlock(final T entity);
}
