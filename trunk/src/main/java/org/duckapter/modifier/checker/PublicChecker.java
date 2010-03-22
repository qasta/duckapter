package org.duckapter.modifier.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.PublicOnlyChecker;
import org.duckapter.modifier.Public;

public class PublicChecker extends DefaultChecker<Public> {

	@Override
	public boolean checkClass(Public anno, Class<?> clazz,
			Class<?> duckInterface) {
		return anno.value().checkPublic(clazz.getModifiers());
	}

	@Override
	protected boolean checkField(Public anno, Field field, Method duckMethod) {
		return anno.value().checkPublic(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(Public anno, Method method, Method duckMethod) {
		return anno.value().checkPublic(method.getModifiers());
	}

	@Override
	public boolean checkConstructor(Public anno, Constructor<?> c,
			Method duckMethod) {
		return anno.value().checkPublic(c.getModifiers());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<PublicOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(PublicOnlyChecker.class);
	}
}
