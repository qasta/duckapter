package org.duckapter.checker;

import java.lang.annotation.Annotation;


public class InterfaceChecker<T extends Annotation> extends DefaultChecker<T> {

	protected boolean checkClass(T anno, java.lang.Class<?> clazz,
			java.lang.Class<?> duckInterface) {
		return duckInterface.isInterface();
	};

	@SuppressWarnings("unchecked")
	public boolean doesCheck(T anno, java.lang.reflect.AnnotatedElement element) {
		return element instanceof Class;
	};

}
