package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ExceptionsChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	@Override
	protected boolean doesCheckField(T anno) {
		return false;
	};

	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return checkExes(method.getExceptionTypes(), duckMethod
				.getExceptionTypes());
	}

	private boolean checkExes(final Class<?>[] originalExs,
			final Class<?>[] duckExs) {
		if (originalExs.length == 0) {
			return true;
		}
		if (duckExs.length == 0 && originalExs.length != 0) {
			return false;
		}
		for (Class<?> originalEx : originalExs) {
			boolean exOk = false;
			for (Class<?> duckEx : duckExs) {
				exOk |= duckEx.isAssignableFrom(originalEx);
			}
			if (!exOk) {
				return false;
			}
		}
		return true;
	};

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return checkExes(constructor.getExceptionTypes(), duckMethod
				.getExceptionTypes());
	};

}
