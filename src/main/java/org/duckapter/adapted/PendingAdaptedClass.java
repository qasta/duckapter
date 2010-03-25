package org.duckapter.adapted;

import org.duckapter.AdaptedClass;

final class PendingAdaptedClass extends AbstractEmptyAdaptedClass implements
		AdaptedClass {

	PendingAdaptedClass(Class<?> duckInterface, Class<?> originalClass) {
		super(duckInterface, originalClass, true);
	}

}
