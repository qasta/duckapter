package org.duckapter.adapted;

import org.duckapter.AdaptationException;
import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

final class EmptyAdapted<O,D> extends AbstractAdapted<O,D> implements Adapted<O,D> {

	EmptyAdapted(O original, AdaptedClass<O,D> adaptedClass) {
		super(original, adaptedClass);
	}
	
	public D adaptClass() {
		throw new AdaptationException(getAdaptedClass());
	}
	
	public D adaptInstance() {
		throw new AdaptationException(getAdaptedClass());
	}

}
