package org.duckapter.adapted;

import org.duckapter.AdaptedClass;

final class EmptyAdaptedClass extends AbstractEmptyAdaptedClass implements AdaptedClass {
	
	EmptyAdaptedClass(Class<?> originalClass, Class<?> duckInterface) {
		super(duckInterface, originalClass, false);
	}

}
