package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.Duckapter;

public class ParametersChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

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
		if (duckMethod.getParameterTypes().length == 1) {
			return checkDuck(field.getType(), duckMethod.getParameterTypes()[0]);
		}
		return false;
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
		return Duckapter.canAdaptInstanceOf(actual, desired);
	};
}
