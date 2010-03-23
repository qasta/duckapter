package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.duckapter.adapted.MethodAdapter;
import org.duckapter.adapted.MethodAdapters;
import org.duckapter.adapted.MethodCallAdapter;

public class MethodsOnlyChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	protected MethodAdapter adaptConstructor(T anno,
			java.lang.reflect.Constructor<?> constructor, Method duckMethod) {
		return MethodAdapters.NULL;
	};

	protected MethodAdapter adaptField(T anno, java.lang.reflect.Field field,
			Method duckMethod) {
		return MethodAdapters.NULL;
	};

	@Override
	protected MethodAdapter adaptMethod(T anno, Method method, Method duckMethod) {
		return new MethodCallAdapter(duckMethod, method);
	};

}
