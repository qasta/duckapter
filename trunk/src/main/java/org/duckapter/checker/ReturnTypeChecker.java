package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.Duckapter;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;

public class ReturnTypeChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	public MethodAdapter check(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			if (duckMethod.getDeclaringClass().isAssignableFrom(
					duckMethod.getReturnType())) {
				return MethodAdapters.OK;
			}
		}
		return super.check(anno, original, duck);
	};

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return checkDuck(constructor.getDeclaringClass(), duckMethod);
	}

	private boolean checkDuck(Class<?> returnType, Method duckMethod) {
		if (void.class.equals(duckMethod.getReturnType())) {
			return true;
		}
		if (Object.class.equals(duckMethod.getReturnType())) {
			return true;
		}
		if (duckMethod.getReturnType().isAssignableFrom(returnType)) {
			return true;
		}
		if (!duckMethod.getReturnType().isInterface()) {
			return false;
		}
		return Duckapter.canAdaptInstanceOf(returnType, duckMethod
				.getReturnType());
	};

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod) {
		return checkDuck(field.getType(), duckMethod);
	};

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return checkDuck(method.getReturnType(), duckMethod);
	};

}
