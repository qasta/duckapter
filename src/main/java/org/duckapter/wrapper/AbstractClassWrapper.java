package org.duckapter.wrapper;

import org.duckapter.ClassWrapper;

abstract class AbstractClassWrapper<O,D> implements ClassWrapper<O,D> {

	protected boolean canAdaptClass = true;
	protected boolean canAdaptInstance = true;
	private final Class<D> duckInterface;
	private final Class<O> originalClass;



	public AbstractClassWrapper(final Class<D> duckInterface, final Class<O> originalClass) {
		this.duckInterface = duckInterface;
		this.originalClass = originalClass;
	}

	public boolean canAdaptClass() {
		return canAdaptClass;
	}

	public boolean canAdaptInstance() {
		return canAdaptInstance;
	}

	public Class<D> getDuckInterface() {
		return duckInterface;
	}

	public Class<O> getOriginalClass() {
		return originalClass;
	}

}