package org.duckapter.adapted;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

abstract class AbstractAdapted implements Adapted {

	private final Object original;
	private final AdaptedClass adaptedClass;

	AbstractAdapted(Object original, AdaptedClass adaptedClass) {
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

}