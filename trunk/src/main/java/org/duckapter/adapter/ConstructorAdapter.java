package org.duckapter.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.duckapter.MethodAdapter;

public class ConstructorAdapter extends DefaultMethodAdapter implements MethodAdapter {

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
	
	@Override
	public int getPriority() {
		return MethodAdapterPriorities.CONSTRUCTOR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constructor == null) ? 0 : constructor.hashCode());
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
		ConstructorAdapter other = (ConstructorAdapter) obj;
		if (constructor == null) {
			if (other.constructor != null)
				return false;
		} else if (!constructor.equals(other.constructor))
			return false;
		return true;
	}
	
	@Override
	public boolean isInvocableOnClass() {
		return true;
	}
	
	@Override
	public boolean isInvocableOnInstance() {
		return true;
	}
	

}
