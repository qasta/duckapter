package org.duckapter.adapter;

import org.duckapter.InvocationAdapter;


public class OptionalAdapter extends EmptyAdapter implements InvocationAdapter {

	public OptionalAdapter(Class<?> returnType) {
		super(returnType);
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
	public int getPriority() {
		return InvocationAdaptersPriorities.OPTIONAL;
	}
	
	

}
