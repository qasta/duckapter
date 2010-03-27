package org.duckapter.adapted;

import java.lang.reflect.Method;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class EmptyAdapted extends AbstractAdapted implements Adapted {

	EmptyAdapted(Object original, AdaptedClass adaptedClass) {
		super(original, adaptedClass);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		throw new UnsupportedOperationException("Cannot invoke method on empty adapted class!");
	}

}
