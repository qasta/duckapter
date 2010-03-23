package org.duckapter.adapted;

import java.lang.reflect.Method;

public class MethodCallAdapter extends DefaultMethodAdapter implements MethodAdapter {

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
	
	@Override
	public int getPriority() {
		return -10;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodCallAdapter other = (MethodCallAdapter) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}
	
	

}
