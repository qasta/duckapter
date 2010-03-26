package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;

public abstract class CheckerBase<T extends Annotation> extends AbstractChecker<T> {

	@SuppressWarnings("unchecked")
	@Override
	public final MethodAdapter  adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (original instanceof Class && duck instanceof Class) {
			return adaptClass(anno, (Class) original, (Class) duck);
		}
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			if (original instanceof Field) {
				return adaptField(anno, (Field) original, duckMethod);
			}
			if (original instanceof Method) {
				return adaptMethod(anno, (Method) original, duckMethod);
			}
			if (original instanceof Constructor) {
				return adaptConstructor(anno, (Constructor) original,
						duckMethod);
			}
		}
		return MethodAdapters.NULL;
	}
	
	protected MethodAdapter adaptClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return MethodAdapters.NULL;
	}

	protected MethodAdapter adaptField(T anno, Field field, Method duckMethod) {
		return MethodAdapters.NULL;
	}

	protected MethodAdapter adaptMethod(T anno, Method method, Method duckMethod) {
		return MethodAdapters.NULL;
	}

	protected MethodAdapter adaptConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return MethodAdapters.NULL;
	};
	
}
