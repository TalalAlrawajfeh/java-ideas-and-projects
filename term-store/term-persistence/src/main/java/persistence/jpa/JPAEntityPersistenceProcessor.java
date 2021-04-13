package persistence.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import persistence.LockMode;
import persistence.EntityPersistenceProcessor;
import persistence.PersistenceEntity;
import persistence.EntityPersistenceField;

public class JPAEntityPersistenceProcessor<J, T extends PersistenceEntity<J>>
		implements EntityPersistenceProcessor<J, T> {
	private JPAEntityPersistenceProvider jpaEntityPersistenceProvider;
	private Map<LockMode, Consumer<T>> lockMap = new HashMap<>();
	private final Consumer<T> lockRead;
	private final Consumer<T> lockWrite;

	public JPAEntityPersistenceProcessor(JPAEntityPersistenceProvider jpaEntityPersistenceProvider) {
		this.jpaEntityPersistenceProvider = jpaEntityPersistenceProvider;

		lockRead = t -> jpaEntityPersistenceProvider.getPersistenceManager().lock(t, LockModeType.PESSIMISTIC_READ);
		lockWrite = t -> jpaEntityPersistenceProvider.getPersistenceManager().lock(t, LockModeType.PESSIMISTIC_WRITE);

		lockMap.put(LockMode.READ, lockRead);
		lockMap.put(LockMode.WRITE, lockWrite);
	}

	@Override
	public T findById(Class<T> entityClass, Long id) {
		return jpaEntityPersistenceProvider.getPersistenceManager().find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByFields(Class<T> entityClass, List<EntityPersistenceField<?>> fields) {
		Query jpaQuery = jpaEntityPersistenceProvider.getPersistenceManager()
				.createQuery("SELECT t FROM " + entityClass.getName() + " t WHERE " + fields.stream()
						.map(f -> "t." + f.getName() + " LIKE :" + f.getName()).collect(Collectors.joining(" AND ")));

		fields.forEach(f -> jpaQuery.setParameter(f.getName(), f.getValue()));
		return jpaQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Class<T> entityClass, int maxResults) {
		Query jpaQuery = jpaEntityPersistenceProvider.getPersistenceManager()
				.createQuery("SELECT t FROM " + entityClass.getName() + " t").setMaxResults(maxResults);

		return jpaQuery.getResultList();
	}

	@Override
	public void save(T entity) {
		jpaEntityPersistenceProvider.getPersistenceManager().persist(entity);
	}

	@Override
	public void update(T entity) {
		jpaEntityPersistenceProvider.getPersistenceManager().merge(entity);
	}

	@Override
	public void delete(T entity) {
		jpaEntityPersistenceProvider.getPersistenceManager().remove(entity);
	}

	@Override
	public void deleteById(Class<T> entityClass, Long id) {
		delete(findById(entityClass, id));
	}

	@Override
	public void lock(T entity, LockMode lockMode) {
		lockMap.get(lockMode).accept(entity);
	}

	@Override
	public void unlock(T entity) {
		jpaEntityPersistenceProvider.getPersistenceManager().lock(entity, LockModeType.NONE);
	}
}
