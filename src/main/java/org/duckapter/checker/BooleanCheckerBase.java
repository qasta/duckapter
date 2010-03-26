package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.duckapter.Checker;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;

public abstract class BooleanCheckerBase<T extends Annotation> extends
		CheckerBase<T> implements Checker<T> {

	
	protected final MethodAdapter adaptClass(T anno, Class<?> clazz,
			Class<?> duckInterface) {
		return toMethodAdapter(checkClass(anno, clazz, duckInterface));
	}

	protected final MethodAdapter adaptField(T anno, Field field, Method duckMethod) {
		return toMethodAdapter(checkField(anno, field, duckMethod));
	}

	protected final MethodAdapter adaptMethod(T anno, Method method, Method duckMethod) {
		return toMethodAdapter(checkMethod(anno, method, duckMethod));
	}

	protected final MethodAdapter adaptConstructor(T anno,
			Constructor<?> constructor, Method duckMethod) {
		return toMethodAdapter(checkConstructor(anno, constructor, duckMethod));
	};

	protected boolean checkClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return false;
	}

	protected boolean checkField(T anno, Field field, Method duckMethod) {
		return false;
	}

	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return false;
	}

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return false;
	};

	protected MethodAdapter toMethodAdapter(boolean b) {
		return booleanToMethodAdapter(b, MethodAdapters.OK);
	}

	protected MethodAdapter booleanToMethodAdapter(boolean b,
			MethodAdapter okAdapter) {
		return b ? okAdapter : MethodAdapters.NULL;
	}
}
