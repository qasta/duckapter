package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.duckapter.adapted.AdaptedFactory;

public class ParametersChecker<T extends Annotation> extends
		BooleanCheckerBase<T> {

	private static final List<ElementType> TARGETS = Arrays.asList(new ElementType[] { ElementType.METHOD,
					ElementType.CONSTRUCTOR });

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return checkParameters(constructor.getParameterTypes(), duckMethod
				.getParameterTypes());
	};

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod) {
		if (duckMethod.getParameterTypes().length == 0) {
			return true;
		}
		if (Modifier.isFinal(field.getModifiers())) {
			return false;
		}
		if (duckMethod.getParameterTypes().length == 1) {
			return checkDuck(field.getType(), duckMethod.getParameterTypes()[0]);
		}
		return false;
	};

	@Override
	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno != null) {
			return super.getTargetElements(anno);
		}
		return TARGETS;
	};

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return checkParameters(method.getParameterTypes(), duckMethod
				.getParameterTypes());
	}

	private boolean checkParameters(Class<?>[] originalTypes,
			Class<?>[] duckMethodTypes) {
		if (originalTypes.length != duckMethodTypes.length) {
			return false;
		}
		for (int i = 0; i < originalTypes.length; i++) {
			if (!checkDuck(originalTypes[i], duckMethodTypes[i])) {
				return false;
			}
		}
		return true;
	};

	private boolean checkDuck(Class<?> desired, Class<?> actual) {
		if (desired.isAssignableFrom(actual)) {
			return true;
		}
		return AdaptedFactory.adapt(actual, desired)
		.canAdaptInstance();
	};
}
