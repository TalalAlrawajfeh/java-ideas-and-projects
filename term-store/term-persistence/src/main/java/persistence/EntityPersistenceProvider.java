package persistence;

public interface EntityPersistenceProvider<T> {
	void initiateProvider();

	void commitChanges();

	void discardChanges();

	void terminateProvider();

	T getPersistenceManager();
}
