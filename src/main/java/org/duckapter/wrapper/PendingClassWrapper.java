package org.duckapter.wrapper;

import org.duckapter.ClassWrapper;

final class PendingClassWrapper<O,D> extends AbstractEmptyClassWrapper<O,D>implements
		ClassWrapper<O,D> {

	PendingClassWrapper(Class<O> originalClass, Class<D> duckInterface) {
		super(duckInterface, originalClass, true);
	}

}
