package org.duckapter.adapted;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;
import org.duckapter.InvocationAdapter;

final class AdaptedImpl<O, D> extends AbstractAdapted<O, D> implements
		Adapted<O, D>, InvocationHandler {

	private final Map<Method, InvocationAdapter> adaptersCache;
	
	AdaptedImpl(O originalInstance, AdaptedClass<O, D> adaptedClass) {
		super(originalInstance, adaptedClass);
		adaptersCache = adaptedClass.getAdapters();
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return adaptersCache.get(method).invoke(getOriginalInstance(), args);
	}

}
