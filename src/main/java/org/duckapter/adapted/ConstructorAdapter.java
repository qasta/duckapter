package org.duckapter.adapted;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class ConstructorAdapter extends DefaultMethodAdapter implements MethodAdapter {

	private final Constructor<?> constructor;

	public ConstructorAdapter(Method duckMethod, Constructor<?> constructor) {
		super(duckMethod.getReturnType());
		this.constructor = constructor;
		constructor.setAccessible(true);
	}

	@Override
	public Object doInvoke(Object obj, Object[] args) throws Throwable {
		return constructor.newInstance(args);
	}

	@Override
	protected Class<?>[] getParameterTypes() {
		return constructor.getParameterTypes();
	}

}
