package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Private;

public class PrivateChecker extends DefaultChecker<Private> {
	@Override
	public boolean checkClass(Private anno, Class<?> clazz,
			Class<?> duckInterface) {
		return anno.value().checkPrivate(clazz.getModifiers());
	}

	@Override
	protected boolean checkField(Private anno, Field field, Method duckMethod) {
		return anno.value().checkPrivate(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(Private anno, Method method, Method duckMethod) {
		return anno.value().checkPrivate(method.getModifiers());
	}

	@Override
	public boolean checkConstructor(Private anno, Constructor<?> c,
			Method duckMethod) {
		return anno.value().checkPrivate(c.getModifiers());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<PublicOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(PublicOnlyChecker.class);
	}

}
