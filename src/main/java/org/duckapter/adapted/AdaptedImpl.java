package org.duckapter.adapted;

import java.lang.reflect.Method;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class AdaptedImpl implements Adapted {

	private final Object originalInstance;
	private final AdaptedClass adaptedClass;

	AdaptedImpl(Object originalInstance, AdaptedClass adaptedClass) {
		this.originalInstance = originalInstance;
		this.adaptedClass = adaptedClass;
	}

	@Override
	public Object getOriginalInstance() {
		return originalInstance;
	}

	@Override
	public AdaptedClass getAdaptedClass() {
		return adaptedClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (Adapted.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		if (AdaptedClass.class.equals(method.getDeclaringClass())) {
			return method.invoke(adaptedClass, args);
		}
		if (method.getDeclaringClass().isAssignableFrom(adaptedClass.getOriginalClass())) {
			return method.invoke(originalInstance, args);
		}
		if (method.getDeclaringClass().isAssignableFrom(adaptedClass.getDuckInterface())) {
			return adaptedClass.invoke(originalInstance, method, args);
		}
		throw new UnsupportedOperationException("Operation not supported: "
				+ method);
	}

}
