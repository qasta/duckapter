package org.duckapter;

import java.lang.reflect.InvocationHandler;

public interface Adapted<O,D> extends InvocationHandler {

	O getOriginalInstance();

	AdaptedClass<O,D> getAdaptedClass();
	
	D adaptInstance();
	D adaptClass();

}
