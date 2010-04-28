package org.duckapter.adapted;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class AdaptedImpl<O, D> extends AbstractAdapted<O, D> implements
		Adapted<O, D>, InvocationHandler {

	AdaptedImpl(O originalInstance, AdaptedClass<O, D> adaptedClass) {
		super(originalInstance, adaptedClass);
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		final Class<?> declaringClass = method.getDeclaringClass();
		if (Adapted.class.equals(declaringClass)) {
			return method.invoke(this, args);
		}
		if (AdaptedClass.class.equals(declaringClass)) {
			return method.invoke(getAdaptedClass(), args);
		}
		if (declaringClass.isAssignableFrom(getAdaptedClass()
				.getDuckInterface())) {
			return getAdaptedClass()
					.invoke(getOriginalInstance(), method, args);
		}
		if (declaringClass.isAssignableFrom(getAdaptedClass()
				.getOriginalClass())) {
			return method.invoke(getOriginalInstance(), args);
		}
		throw new UnsupportedOperationException("Operation not supported: "
				+ method);
	}
	
	

}
