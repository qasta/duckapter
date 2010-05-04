package org.duckapter.wrapper;

import org.duckapter.ClassWrapper;

final class EmptyClassWrapper<O,D> extends AbstractEmptyClassWrapper<O,D> implements ClassWrapper<O,D> {
	
	EmptyClassWrapper(Class<O> originalClass, Class<D> duckInterface) {
		super(duckInterface, originalClass, false);
	}

}
