package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.InvocationAdapter;
import org.duckapter.LogicalChecker;
import org.duckapter.adapter.InvocationAdapters;

public abstract class LogicalCheckerBase<T extends Annotation> extends
		AbstractChecker<T> implements LogicalChecker<T> {

	public final InvocationAdapter adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		return toMethodAdapter(check(anno, original, duck, classOfOriginal));
	}

	@SuppressWarnings("unchecked")
	public final boolean check(T anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		if (original instanceof Class && duck instanceof Class) {
			return checkClass(anno, (Class) original, (Class) duck);
		}
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			if (original instanceof Field) {
				return checkField(anno, (Field) original, duckMethod,
						classOfOriginal);
			}
			if (original instanceof Method) {
				return checkMethod(anno, (Method) original, duckMethod,
						classOfOriginal);
			}
			if (original instanceof Constructor) {
				return checkConstructor(anno, (Constructor) original,
						duckMethod, classOfOriginal);
			}
		}
		return false;
	}

	protected boolean checkClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return false;
	}

	protected boolean checkField(T anno, Field field, Method duckMethod,
			Class<?> classOfOriginal) {
		return false;
	}

	protected boolean checkMethod(T anno, Method method, Method duckMethod,
			Class<?> classOfOriginal) {
		return false;
	}

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod, Class<?> classOfOriginal) {
		return false;
	};

	protected InvocationAdapter toMethodAdapter(boolean b) {
		return booleanToMethodAdapter(b, InvocationAdapters.OK);
	}

	protected InvocationAdapter booleanToMethodAdapter(boolean b,
			InvocationAdapter okAdapter) {
		return b ? okAdapter : InvocationAdapters.NULL;
	}
}
