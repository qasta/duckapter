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

}