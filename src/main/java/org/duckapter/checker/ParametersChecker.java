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

import org.duckapter.wrapper.WrapperFactory;

/**
 * The default checker which checks whether the target element's parameters are
 * compatible with the ones declared by the duck method.<br/>
 * The target element's parameters are compatibile if
 * <ul>
 * <li>the count of the parameters are the same
 * <li>the types from the duck interface can be assigned to the types from the
 * target element or can be adapted to these parameters
 * </ul>
 * 
 * 
 * @author Vladimir Orany
 * 
 * @param <T>
 *            any checker annotation type
 */
public class ParametersChecker<T extends Annotation> extends
		LogicalCheckerBase<T> {

	private static final List<ElementType> TARGETS = Arrays
			.asList(new ElementType[] { ElementType.METHOD,
					ElementType.CONSTRUCTOR });

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod, Class<?> classOfOriginal) {
		return checkParameters(constructor.getParameterTypes(), duckMethod
				.getParameterTypes());
	};

	@Override
	protected boolean checkField(T anno, Field field, Method duckMethod,
			Class<?> classOfOriginal) {
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
	protected boolean checkMethod(T anno, Method method, Method duckMethod,
			Class<?> classOfOriginal) {
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
		return WrapperFactory.adapt(actual, desired).canAdaptInstance();
	};
}
