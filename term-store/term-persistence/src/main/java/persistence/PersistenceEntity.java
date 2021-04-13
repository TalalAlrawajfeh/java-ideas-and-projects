package persistence;

public interface PersistenceEntity<T> {
	Long getId();

	T acquireBean();
}
