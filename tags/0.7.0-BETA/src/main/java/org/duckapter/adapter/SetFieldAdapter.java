package org.duckapter.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.InvocationAdapter;

/**
 * This adapter sets field content as the result of invocation.
 * 
 * @author Vladimir Orany
 * @see org.duckapter.annotation.Field
 * @see org.duckapter.checker.FieldChecker
 */
public class SetFieldAdapter extends AbstractInvocationAdapter implements
		InvocationAdapter {

	private final Field field;

	/**
	 * @param duckMethod
	 *            the duck method
	 * @param field
	 *            the field to be adapted
	 */
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

	public int getPriority() {
		return InvocationAdaptersPriorities.FIELD;
	}

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
		SetFieldAdapter other = (SetFieldAdapter) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}

	@Override
	public boolean isInvocableOnClass() {
		return Modifier.isStatic(field.getModifiers());
	}

}
