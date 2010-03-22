package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.Duckapter;

public class ReturnTypeChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	public boolean check(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			if (duckMethod.getDeclaringClass().isAssignableFrom(duckMethod.getReturnType())) {
				return true;
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
		if (duckMethod.getReturnType().isAssignableFrom(returnType)) {
			return true;
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
