package org.duckapter.annotation.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.adapted.MethodAdapter;
import org.duckapter.adapted.MethodAdapters;
import org.duckapter.adapted.MethodCallAdapter;
import org.duckapter.annotation.Property;
import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.MethodsOnlyChecker;

public class PropertyChecker extends DefaultChecker<Property> {

	private static final FieldChecker checker = new FieldChecker();

	@Override
	protected boolean doesCheckClass(Property anno) {
		return false;
	}

	@Override
	protected MethodAdapter adaptField(Property anno, Field field, Method duckMethod) {
		return checker.check(null, field, duckMethod);
	}

	@Override
	protected MethodAdapter adaptMethod(Property anno, Method method,
			Method duckMethod) {
		if( duckMethod.getParameterTypes().length == 0
				|| duckMethod.getParameterTypes().length == 1) {
			return new MethodCallAdapter(duckMethod, method);
		}
		return MethodAdapters.NULL;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<MethodsOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class);
	}

}
