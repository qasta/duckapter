package org.duckapter.wrapper;

import org.duckapter.WrappingException;
import org.duckapter.ObjectWrapper;
import org.duckapter.ClassWrapper;

final class EmptyObjectWrapper<O,D> extends AbstractObjectWrapper<O,D> implements ObjectWrapper<O,D> {

	EmptyObjectWrapper(O original, ClassWrapper<O,D> adaptedClass) {
		super(original, adaptedClass);
	}
	
	public D adaptClass() {
		throw new WrappingException(this);
	}
	
	public D adaptInstance() {
		throw new WrappingException(this);
	}

}
