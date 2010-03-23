package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NameChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	protected boolean checkConstructor(T anno,
			java.lang.reflect.Constructor<?> constructor, Method duckMethod) {
		return checkFieldName(duckMethod.getName(), constructor.getName());
	};

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod) {
		return checkFieldName(duckMethod.getName(), field.getName());
	}

	protected boolean checkFieldName(final String duckName,
			final String fieldName) {
		return normalize(duckName).equals(normalize(fieldName))
				|| normalize(duckName).equals("is" + normalize(fieldName))
				|| normalize(duckName).equals("get" + normalize(fieldName))
				|| normalize(duckName).equals("set" + normalize(fieldName));
	};

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		final String methodName = method.getName();
		final String duckName = duckMethod.getName();
		return checkMethodName(duckName, methodName);
	}

	protected boolean checkMethodName(final String duckName,
			final String methodName) {
		return normalize(methodName).equals(normalize(duckName));
	};

	protected String normalize(String s) {
		return s.toLowerCase().replace("_", "");
	}

}
