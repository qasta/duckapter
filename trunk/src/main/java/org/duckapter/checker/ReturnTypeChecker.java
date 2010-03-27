package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.duckapter.adapted.AdaptedFactory;

public class ReturnTypeChecker<T extends Annotation> extends
		BooleanCheckerBase<T> {

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		if (duckMethod.getDeclaringClass().isAssignableFrom(duckMethod.getReturnType())) {
			return true;
		}
		return checkDuck(constructor.getDeclaringClass(), duckMethod);
	}

	private boolean checkDuck(Class<?> returnType, Method duckMethod) {
		if (void.class.equals(duckMethod.getReturnType())) {
			return true;
		}
		if (void.class.equals(returnType)) {
			return false;
		}
		if (Object.class.equals(duckMethod.getReturnType())) {
			return true;
		}
		if (duckMethod.getReturnType().isAssignableFrom(returnType)) {
			return true;
		}
		if (duckMethod.getReturnType().isPrimitive() && !returnType.isPrimitive()) {
			return false;
		}
		if (!duckMethod.getReturnType().isInterface()) {
			return false;
		}
		return AdaptedFactory.adapt(returnType, duckMethod
				.getReturnType())
		.canAdaptInstance();
	};

	@Override
	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno != null) {
			return super.getTargetElements(anno);
		}
		return Arrays.asList(new ElementType[] { ElementType.METHOD,
				ElementType.CONSTRUCTOR, ElementType.FIELD });
	};

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod) {
		if (duckMethod.getDeclaringClass().isAssignableFrom(duckMethod.getReturnType())
				&& field.getDeclaringClass().isAssignableFrom(field.getType())
		) {
			return true;
		}
		return checkDuck(field.getType(), duckMethod);
	};

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		if (duckMethod.getDeclaringClass().isAssignableFrom(duckMethod.getReturnType())
				&& method.getDeclaringClass().isAssignableFrom(method.getReturnType())
		) {
			return true;
		}
		return checkDuck(method.getReturnType(), duckMethod);
	};

}
