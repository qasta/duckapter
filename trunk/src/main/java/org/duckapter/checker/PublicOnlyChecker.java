package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.annotation.Visibility;

public class PublicOnlyChecker<T extends Annotation> extends DefaultChecker<T> {
	@Override
	public boolean checkClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return Visibility.EXACT.checkPublic(clazz.getModifiers());
	}

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod) {
		return Visibility.EXACT.checkPublic(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return Visibility.EXACT.checkPublic(method.getModifiers());
	}

	@Override
	public boolean checkConstructor(T anno, Constructor<?> c, Method duckMethod) {
		return Visibility.EXACT.checkPublic(c.getModifiers());
	}
}
