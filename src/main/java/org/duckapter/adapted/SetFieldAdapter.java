package org.duckapter.adapted;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class SetFieldAdapter extends DefaultMethodAdapter implements MethodAdapter {

	private final Field field;

	public SetFieldAdapter(Method duckMethod, Field field) {
		super(duckMethod.getReturnType());
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public Object doInvoke(Object obj, Object[] args) throws Throwable {
		field.set(obj, args[0]);
		return null;
	}

	@Override
	protected Class<?>[] getParameterTypes() {
		return new Class<?>[] { field.getType() };
	}

}
