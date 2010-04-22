package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.ConstructorAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.Constructor;

public class ConstructorChecker extends AbstractChecker<Constructor> {

	public InvocationAdapter adapt(Constructor anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		if (!(original instanceof java.lang.reflect.Constructor<?>)
				|| !(duck instanceof Method)) {
			return InvocationAdapters.NULL;
		}
		java.lang.reflect.Constructor<?> constructor = (java.lang.reflect.Constructor<?>) original;
		try {
			constructor.setAccessible(true);
		} catch (SecurityException e) {
			return InvocationAdapters.NULL;
		}
		return new ConstructorAdapter((Method) duck, constructor);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends AbstractChecker>> suppressCheckers(
			Constructor anno, AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class, NameChecker.class);
	}

}
