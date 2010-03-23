package org.duckapter.adapted;

import java.lang.reflect.Method;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

class EmptyAdapted implements Adapted {

	private final Object original;
	private final AdaptedClass adaptedClass;
	
	public EmptyAdapted(Object original, AdaptedClass adaptedClass) {
		this.original = original;
		this.adaptedClass = adaptedClass;
	}

	@Override
	public AdaptedClass getAdaptedClass() {
		return adaptedClass;
	}

	@Override
	public Object getOriginalInstance() {
		return original;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		throw new UnsupportedOperationException("Cannot invoke method on empty adapted class!");
	}

}
