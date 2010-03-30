package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;

public abstract class CheckerBase<T extends Annotation> extends AbstractChecker<T> {

	@SuppressWarnings("unchecked")
	@Override
	public final InvocationAdapter  adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
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
		return InvocationAdapters.NULL;
	}
	
	protected InvocationAdapter adaptClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return InvocationAdapters.NULL;
	}

	protected InvocationAdapter adaptField(T anno, Field field, Method duckMethod) {
		return InvocationAdapters.NULL;
	}

	protected InvocationAdapter adaptMethod(T anno, Method method, Method duckMethod) {
		return InvocationAdapters.NULL;
	}

	protected InvocationAdapter adaptConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return InvocationAdapters.NULL;
	};
	
}
