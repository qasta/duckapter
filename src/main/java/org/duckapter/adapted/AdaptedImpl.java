package org.duckapter.adapted;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.duckapter.AdaptationException;
import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class AdaptedImpl<O, D> extends AbstractAdapted<O, D> implements
		Adapted<O, D>, InvocationHandler {

	AdaptedImpl(O originalInstance, AdaptedClass<O, D> adaptedClass) {
		super(originalInstance, adaptedClass);
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (Adapted.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		if (AdaptedClass.class.equals(method.getDeclaringClass())) {
			return method.invoke(getAdaptedClass(), args);
		}
		if (method.getDeclaringClass().isAssignableFrom(
				getAdaptedClass().getDuckInterface())) {
			return getAdaptedClass()
					.invoke(getOriginalInstance(), method, args);
		}
		if (method.getDeclaringClass().isAssignableFrom(
				getAdaptedClass().getOriginalClass())) {
			return method.invoke(getOriginalInstance(), args);
		}
		throw new UnsupportedOperationException("Operation not supported: "
				+ method);
	}

	public D adaptInstance() {
		if (!getAdaptedClass().canAdaptInstance()) {
			throw new AdaptationException(this);
		}
		return createProxy();
	}

	
	public D adaptClass() {
		if (!getAdaptedClass().canAdaptClass()) {
			throw new AdaptationException(this);
		}
		return createProxy();
	}

	private D createProxy() {
		return getAdaptedClass().getDuckInterface().cast(
				Proxy.newProxyInstance(getClass().getClassLoader(),
						new Class[] { getAdaptedClass().getDuckInterface(),
								Adapted.class, AdaptedClass.class }, this));
	}

}
