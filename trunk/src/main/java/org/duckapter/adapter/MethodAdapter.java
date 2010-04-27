package org.duckapter.adapter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.InvocationAdapter;

/**
 * The adapter for the methods. Wraps method call to be invokable using
 * {@link InvocationAdapter} interface. The adapter is always invokable on the
 * instance and it is invokable on the class if the wrapped method is static.
 * 
 * @author Vladimir Orany
 * @see org.duckapter.annotation.Method
 * @see org.duckapter.checker.MethodChecker
 */
public class MethodAdapter extends AbstractInvocationAdapter implements
		InvocationAdapter {

	private final Method method;

	/**
	 * @param duckMethod
	 *            the duck method
	 * @param method
	 *            the method to be adapted
	 */
	public MethodAdapter(Method duckMethod, Method method) {
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

	public int getPriority() {
		return InvocationAdaptersPriorities.METHOD;
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
		MethodAdapter other = (MethodAdapter) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public boolean isInvocableOnClass() {
		return Modifier.isStatic(method.getModifiers());
	}

	@Override
	public String toString() {
		return "MethodAdapter[" + method + "]";
	}

}
