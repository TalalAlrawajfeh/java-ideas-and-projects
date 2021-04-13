package com.tools.parsers.utility;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SAXDataFileParserHandlerReflectionUtility {
	private static final String SET_PREFIX = "set";
	private static final String WORD_PART_REGEX = "(\\w{1})(\\w+)(-*)";
	private static final int WORD_FIRST_LETTER_GROUP_INDEX = 1;
	private static final int WORD_REMAINING_LETTERS_GROUP_INDEX = 2;

	public static <T> void invokeSetterMethodIfPresent(Object object, String setterMethodName, T valueToSet)
			throws Exception {
		Optional<Method> setterMethod = Stream.of(object.getClass().getDeclaredMethods())
				.filter(m -> m.getName().equals(setterMethodName)).findAny();
		if (setterMethod.isPresent()) {
			setterMethod.get().invoke(object, valueToSet);
		}
	}

	public static String getSetterNameFromElementName(String elementName) {
		String setterName = SET_PREFIX;
		Matcher matcher = Pattern.compile(WORD_PART_REGEX).matcher(elementName);
		while (matcher.find()) {
			setterName += matcher.group(WORD_FIRST_LETTER_GROUP_INDEX).toUpperCase()
					+ matcher.group(WORD_REMAINING_LETTERS_GROUP_INDEX);
		}
		return setterName;
	}
}
