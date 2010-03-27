package org.duckapter;

import java.lang.reflect.Method;
import java.util.Collection;



public interface AdaptedClass<O,D> {

	Object invoke(O object, Method duckMethod, Object[] args)
			throws Throwable;

	Class<O> getOriginalClass();
	Class<D> getDuckInterface();

	/* nesmi mit zadny staticky/konstruktory */
	boolean canAdaptInstance();
	boolean canAdaptClass();

	Collection<Method> getUnimplementedForInstance();
	Collection<Method> getUnimplementedForClass();

	
}
