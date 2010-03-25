package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Protected;

public class ProtectedChecker extends DefaultChecker<Protected> {
	@Override
	public boolean checkClass(Protected anno, Class<?> clazz,
			Class<?> duckInterface) {
		return anno.value().checkProtected(clazz.getModifiers());
	}

	@Override
	protected boolean checkField(Protected anno, Field field, Method duckMethod) {
		return anno.value().checkProtected(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(Protected anno, Method method,
			Method duckMethod) {
		return anno.value().checkProtected(method.getModifiers());
	}

	@Override
	public boolean checkConstructor(Protected anno, Constructor<?> c,
			Method duckMethod) {
		return anno.value().checkProtected(c.getModifiers());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<PublicOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(PublicOnlyChecker.class);
	}
}
