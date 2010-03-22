package org.duckapter.adapted;

interface MethodAdapter {

	Object invoke(Object obj, Object[] args) throws Throwable;
	
	int priority();
	
	void replaces(MethodAdapter adapter);

}
