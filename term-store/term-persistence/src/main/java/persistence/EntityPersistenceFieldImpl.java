package persistence;

public class EntityPersistenceFieldImpl<T> implements EntityPersistenceField<T> {
	private String name;
	private T value;

	public EntityPersistenceFieldImpl(String name, T value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getValue() {
		return value;
	}
}
