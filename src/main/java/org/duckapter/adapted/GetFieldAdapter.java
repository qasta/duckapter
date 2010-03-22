package org.duckapter.adapted;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class GetFieldAdapter extends DefaultMethodAdapter implements MethodAdapter {

	private final Field field;

	public GetFieldAdapter(Method duckMethod, final Field field) {
		super(duckMethod.getReturnType());
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public Object doInvoke(Object obj, Object[] args) throws Throwable {
		return field.get(obj);
	}

	@Override
	protected Class<?>[] getParameterTypes() {
		return new Class<?>[0];
	}
	
	@Override
	public int priority() {
		return -1000;
	}
	
	@Override
	public void replaces(MethodAdapter adapter) {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
		GetFieldAdapter other = (GetFieldAdapter) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}
	
	

}
