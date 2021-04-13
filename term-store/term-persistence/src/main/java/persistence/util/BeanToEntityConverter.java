package persistence.util;

public interface BeanToEntityConverter<T, J> {
	J getEntityFromBean(T t);
}
