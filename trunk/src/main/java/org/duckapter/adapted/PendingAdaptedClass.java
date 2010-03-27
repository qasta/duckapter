package org.duckapter.adapted;

import org.duckapter.AdaptedClass;

final class PendingAdaptedClass<O,D> extends AbstractEmptyAdaptedClass<O,D>implements
		AdaptedClass<O,D> {

	PendingAdaptedClass(Class<O> originalClass, Class<D> duckInterface) {
		super(duckInterface, originalClass, true);
	}

}
