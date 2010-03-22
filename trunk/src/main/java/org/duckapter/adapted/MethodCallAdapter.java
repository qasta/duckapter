package org.duckapter.adapted;

import java.lang.reflect.Method;

class MethodCallAdapter extends DefaultMethodAdapter implements MethodAdapter {

	private final Method method;

	public MethodCallAdapter(Method duckMethod, Method method) {
		super(duckMethod.getReturnType());
		this.method = method;
		method.setAccessible(true);
	}

	@Override
	public Object doInvoke(Object obj, Object[] args) throws Throwable {
		return method.invoke(obj, args);
	}

	@Override
	protected Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

}
