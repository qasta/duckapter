package org.duckapter.wrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.duckapter.WrappingException;
import org.duckapter.ObjectWrapper;
import org.duckapter.ClassWrapper;

final class ObjectWrapperImpl<O, D> extends AbstractObjectWrapper<O, D> implements
		ObjectWrapper<O, D>, InvocationHandler {

	ObjectWrapperImpl(O originalInstance, ClassWrapper<O, D> adaptedClass) {
		super(originalInstance, adaptedClass);
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (ObjectWrapper.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		if (ClassWrapper.class.equals(method.getDeclaringClass())) {
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
			throw new WrappingException(this);
		}
		return createProxy();
	}

	
	public D adaptClass() {
		if (!getAdaptedClass().canAdaptClass()) {
			throw new WrappingException(this);
		}
		return createProxy();
	}

	private D createProxy() {
		return getAdaptedClass().getDuckInterface().cast(
				Proxy.newProxyInstance(getClass().getClassLoader(),
						new Class[] { getAdaptedClass().getDuckInterface(),
								ObjectWrapper.class, ClassWrapper.class }, this));
	}

}
