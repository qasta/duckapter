package org.duckapter;

import java.lang.reflect.InvocationHandler;

public interface Adapted extends InvocationHandler {

	Object getOriginalInstance();

	AdaptedClass getAdaptedClass();

}
