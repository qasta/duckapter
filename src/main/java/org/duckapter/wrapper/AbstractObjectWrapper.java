package org.duckapter.wrapper;

import org.duckapter.ObjectWrapper;
import org.duckapter.ClassWrapper;

abstract class AbstractObjectWrapper<O,D> implements ObjectWrapper<O,D> {

	private final O original;
	private final ClassWrapper<O,D> classWrapper;

	AbstractObjectWrapper(O original, ClassWrapper<O,D> adaptedClass) {
		this.original = original;
		this.classWrapper = adaptedClass;
	}

	public ClassWrapper<O,D> getClassWrapper() {
		return classWrapper;
	}

	public O getOriginalInstance() {
		return original;
	}

}