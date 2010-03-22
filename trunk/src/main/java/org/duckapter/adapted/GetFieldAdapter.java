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

}
