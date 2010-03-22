package org.duckapter.adapted;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MethodAdapterFactory {

	private MethodAdapterFactory() {
		// prevents instance creation and subtyping
	}

	@SuppressWarnings("unchecked")
	public static MethodAdapter createMethodAdapter(Method duckMethod,
			AnnotatedElement element) {
		if (element instanceof Constructor) {
			return new ConstructorAdapter(duckMethod, (Constructor) element);

		}
		if (element instanceof Field) {
			if (duckMethod.getParameterTypes().length == 1) {
				return new SetFieldAdapter(duckMethod, (Field) element);
			} else if (duckMethod.getParameterTypes().length == 0) {
				return new GetFieldAdapter(duckMethod, (Field) element);
			}
			return NullAdapter.INSTANCE;
		}
		if (element instanceof Method) {
			return new MethodCallAdapter(duckMethod, (Method) element);
		}
		return NullAdapter.INSTANCE;
	}

}
