package org.duckapter.adapter;

import org.duckapter.InvocationAdapter;

public abstract class EmptyAdapter implements InvocationAdapter{

	private final Class<?> returnType;

	public abstract int getPriority();

	public abstract boolean isInvocableOnInstance();

	public abstract boolean isInvocableOnClass();

	public EmptyAdapter(Class<?> returnType) {
		this.returnType = returnType;
	}

	@Override
	public InvocationAdapter andMerge(InvocationAdapter other) {
		return InvocationAdapters.andMerge(this, other);
	}

	@Override
	public InvocationAdapter orMerge(InvocationAdapter other) {
		return InvocationAdapters.orMerge(this, other);
	}

	@Override
	public Object invoke(Object obj, Object[] args) throws Throwable {
		if (int.class.equals(returnType)){
			return 0;
		}
		if (short.class.equals(returnType)) {
			return (short)0;
		}
		if (byte.class.equals(returnType)) {
			return (byte)0;
		}
		if (long.class.equals(returnType)) {
			return 0L;
		}
		if (char.class.equals(returnType)) {
			return (char)0;
		}
		if (float.class.equals(returnType)) {
			return 0f;
		}
		if (double.class.equals(returnType)) {
			return 0d;
		}
		if (boolean.class.equals(returnType)) {
			return false;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((returnType == null) ? 0 : returnType.hashCode());
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
		EmptyAdapter other = (EmptyAdapter) obj;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}

	
	
}