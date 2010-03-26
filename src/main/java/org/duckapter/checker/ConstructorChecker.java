package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.ConstructorAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.annotation.Constructor;

public class ConstructorChecker extends AbstractChecker<Constructor> {

	@Override
	public MethodAdapter adapt(Constructor anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (!(original instanceof java.lang.reflect.Constructor<?>)
				|| !(duck instanceof Method)) {
			return MethodAdapters.NULL;
		}
		java.lang.reflect.Constructor<?> constructor = (java.lang.reflect.Constructor<?>) original;
		try {
			constructor.setAccessible(true);
		} catch (SecurityException e) {
			return MethodAdapters.NULL;
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
