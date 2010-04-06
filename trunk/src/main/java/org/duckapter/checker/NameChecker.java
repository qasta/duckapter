package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class NameChecker<T extends Annotation> extends LogicalCheckerBase<T> {

	protected boolean checkConstructor(T anno,
			java.lang.reflect.Constructor<?> constructor, Method duckMethod, Class<?> classOfOriginal) {
		return checkMethodName(duckMethod.getName(), constructor.getName());
	};

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod, Class<?> classOfOriginal) {
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
	protected boolean checkMethod(T anno, Method method, Method duckMethod, Class<?> classOfOriginal) {
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

	@Override
	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno != null) {
			return super.getTargetElements(anno);
		}
		return Arrays.asList(new ElementType[] { ElementType.METHOD,
				ElementType.FIELD, ElementType.CONSTRUCTOR });
	};

}
