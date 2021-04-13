package persistence;

public interface EntityPersistenceField<T> {
	String getName();

	T getValue();
}
