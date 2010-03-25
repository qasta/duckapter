package org.duckapter.adapter;

import org.duckapter.MethodAdapter;


public class OptionalAdapter implements MethodAdapter {

	private final Class<?> returnType;
	
	public OptionalAdapter(Class<?> returnType) {
		this.returnType = returnType;
	}
	
	@Override
	public MethodAdapter andMerge(MethodAdapter other) {
		return MethodAdapters.andMerge(this, other);
	}
	
	@Override
	public MethodAdapter orMerge(MethodAdapter other) {
		return MethodAdapters.orMerge(this, other);
	}
	
	@Override
	public boolean isInvocableOnClass() {
		return true;
	}
	
	@Override
	public boolean isInvocableOnInstance() {
		return true;
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
	public int getPriority() {
		return MethodAdapterPriorities.OPTIONAL;
	}

}
