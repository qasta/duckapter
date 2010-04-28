package org.duckapter.adapted;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.duckapter.AdaptationException;
import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class AdaptedImpl<O, D> extends AbstractAdapted<O, D> implements
		Adapted<O, D>, InvocationHandler {

	private D proxy;

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
					.invoke((O) proxy, method, args);
		}
		if (declaringClass.isAssignableFrom(getAdaptedClass()
				.getOriginalClass())) {
			return method.invoke(proxy, args);
		}
		throw new UnsupportedOperationException("Operation not supported: "
				+ method);
	}

	public D adaptInstance() {
		if (!getAdaptedClass().canAdaptInstance()) {
			throw new AdaptationException(this);
		}
		return getProxy();
	}

	private D getProxy() {
		if (proxy == null) {
			proxy = createProxy();
		}
		return proxy;
	}

	public D adaptClass() {
		if (!getAdaptedClass().canAdaptClass()) {
			throw new AdaptationException(this);
		}
		return getProxy();
	}

	private D createProxy() {
		return getAdaptedClass().getDuckInterface().cast(
				Proxy.newProxyInstance(getClass().getClassLoader(),
						new Class[] { getAdaptedClass().getDuckInterface(),
								Adapted.class, AdaptedClass.class }, this));
	}

}
