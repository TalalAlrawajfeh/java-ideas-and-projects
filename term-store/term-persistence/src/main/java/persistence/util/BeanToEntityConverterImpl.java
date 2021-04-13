package persistence.util;

import java.util.stream.Stream;

public class BeanToEntityConverterImpl<T, J> implements BeanToEntityConverter<T, J> {
	@SuppressWarnings("unchecked")
	@Override
	public J getEntityFromBean(T t) {
		J j = null;
		try {
			j = (J) Stream.of(this.getClass().getDeclaredMethods()).filter(m -> "getEntityFromBean".equals(m.getName()))
					.findAny().get().getReturnType().newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
