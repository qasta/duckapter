package org.duckapter.adapted;

import java.lang.reflect.Method;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class AdaptedImpl extends AbstractAdapted implements Adapted {

	AdaptedImpl(Object originalInstance, AdaptedClass adaptedClass) {
		super(originalInstance, adaptedClass);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (Adapted.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		if (AdaptedClass.class.equals(method.getDeclaringClass())) {
			return method.invoke(getAdaptedClass(), args);
		}
		if (method.getDeclaringClass().isAssignableFrom(
				getAdaptedClass().getOriginalClass())) {
			return method.invoke(getOriginalInstance(), args);
		}
		if (method.getDeclaringClass().isAssignableFrom(
				getAdaptedClass().getDuckInterface())) {
			return getAdaptedClass()
					.invoke(getOriginalInstance(), method, args);
		}
		throw new UnsupportedOperationException("Operation not supported: "
				+ method);
	}

}
