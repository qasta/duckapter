package org.duckapter.annotation.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
	protected boolean checkField(Property anno, Field field, Method duckMethod) {
		return checker.checkField(null, field, duckMethod);
	}

	@Override
	protected boolean checkMethod(Property anno, Method method,
			Method duckMethod) {
		return duckMethod.getParameterTypes().length == 0
				|| duckMethod.getParameterTypes().length == 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<MethodsOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class);
	}

}
