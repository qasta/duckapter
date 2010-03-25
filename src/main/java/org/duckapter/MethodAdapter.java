package org.duckapter;

public interface MethodAdapter {

	Object invoke(Object obj, Object[] args) throws Throwable;
	
	int getPriority();
	
	MethodAdapter orMerge(MethodAdapter other);
	MethodAdapter andMerge(MethodAdapter other);
	
	boolean isInvocableOnInstance();
	boolean isInvocableOnClass();

}
