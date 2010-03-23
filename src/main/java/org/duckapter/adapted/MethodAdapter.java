package org.duckapter.adapted;

public interface MethodAdapter {

	Object invoke(Object obj, Object[] args) throws Throwable;
	
	int getPriority();
	
	MethodAdapter mergeWith(MethodAdapter other);

}
