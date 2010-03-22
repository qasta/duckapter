package org.duckapter;

import java.lang.reflect.Method;
import java.util.Collection;



public interface AdaptedClass {

	Object invoke(Object originalInstance, Method duckMethod, Object[] args)
			throws Throwable;

	Class<?> getOriginalClass();

	Class<?> getDuckInterface();

	/* nesmi mit zadny staticky/konstruktory */
	boolean canAdaptInstance();

	boolean canAdaptClass();

	Collection<AdaptationViolation> getAdaptationViolations();

}
