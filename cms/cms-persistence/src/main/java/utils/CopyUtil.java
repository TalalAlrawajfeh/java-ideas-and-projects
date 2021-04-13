package utils;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CopyUtil {
	private static final String CREATE_AND_COPY_FIELDS_ERROR = "Create And Copy Fields Error";
	private static final String COPY_FIELDS_ERROR = "Copy Fields Error";
	private static final String SET_PREFIX = "set";
	private static final String GET_PREFIX = "get";

	private static Logger logger = Logger.getLogger(CopyUtil.class);

	private CopyUtil() {
		/* static class */
	}

	public static <D, S> D createAndCopyFields(Class<D> destionationClass, S source) {
		try {
			D destination = destionationClass.newInstance();
			copyFields(destination, source, null, null, 1);

			return destination;
		} catch (Exception e) {
			logger.error(CREATE_AND_COPY_FIELDS_ERROR, e);

			throw new UtilException(e);
		}
	}

	public static <D, S> void copyFields(D destination, S source, CopySetting[] copySettings, Class<?>[] discardClasses,
			int depth) {

		if (depth == 0) {
			return;
		}

		Stream.of(source.getClass().getDeclaredFields())
				.forEach(f -> handleField(destination, source, copySettings, discardClasses, depth, f));
	}

	private static <D, S> void handleField(D destination, S source, CopySetting[] copySettings,
			Class<?>[] discardClasses, int depth, Field f) {
		
		if (Objects.nonNull(discardClasses)
				&& Stream.of(discardClasses).filter(f.getType()::equals).findAny().isPresent()) {
			return;
		}

		Method getter = findMethodByName(source.getClass(), getFieldGetterName(f));
		Method setter = findMethodByName(destination.getClass(), getFieldSetterName(f));

		if (Objects.isNull(getter) || Objects.isNull(setter)) {
			return;
		}

		try {
			if (Objects.isNull(copySettings)) {
				copyField(destination, source, getter, setter);
				return;
			}

			Optional<CopySetting> copySetting = Stream.of(copySettings).filter(c -> c.getFrom().equals(f.getType()))
					.findAny();

			if (!copySetting.isPresent()) {
				copyField(destination, source, getter, setter);
				return;
			}

			Object fieldValue = getter.invoke(source);

			if (Objects.nonNull(fieldValue)) {
				Object instance = copySetting.get().getTo().newInstance();
				copyFields(instance, fieldValue, copySettings, discardClasses, depth - 1);
				setter.invoke(destination, instance);
			}
		} catch (Exception e) {
			logger.error(COPY_FIELDS_ERROR, e);

			throw new UtilException(e);
		}
	}

	private static <S, D> void copyField(D destination, S source, Method getter, Method setter)
			throws InvocationTargetException, IllegalAccessException {
		Object returnValue = getter.invoke(source);

		if (Objects.nonNull(returnValue)) {
			setter.invoke(destination, getter.getReturnType().cast(returnValue));
		}
	}

	private static String getFieldGetterName(Field field) {
		return GET_PREFIX + capitalizeFirstLetter(field.getName());
	}

	private static String getFieldSetterName(Field field) {
		return SET_PREFIX + capitalizeFirstLetter(field.getName());
	}

	private static String capitalizeFirstLetter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	private static <T> Method findMethodByName(Class<T> clazz, String name) {
		return Stream.of(clazz.getDeclaredMethods()).filter(m -> name.equals(m.getName())).findAny().orElse(null);
	}
}
