package org.duckapter.adapted;

import java.lang.annotation.Annotation;

import org.duckapter.AdaptedClass;
import org.duckapter.Checker;

final class EmptyAdaptedClass extends AbstractEmptyAdaptedClass implements AdaptedClass {

	public static interface InterfaceChecker<T extends Annotation> extends Checker<T>{}
	
	EmptyAdaptedClass(Class<?> originalClass, Class<?> duckInterface) {
		super(duckInterface, originalClass, false);
	}

}
