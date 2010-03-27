package org.duckapter;

public interface InvocationAdapter {

	Object invoke(Object obj, Object[] args) throws Throwable;
	
	int getPriority();
	
	InvocationAdapter orMerge(InvocationAdapter other);
	InvocationAdapter andMerge(InvocationAdapter other);
	
	boolean isInvocableOnInstance();
	boolean isInvocableOnClass();

}
