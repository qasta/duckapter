package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.adapter.MethodCallAdapter;
import org.duckapter.annotation.Property;

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
