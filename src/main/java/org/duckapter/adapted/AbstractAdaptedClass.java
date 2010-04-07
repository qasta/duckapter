package org.duckapter.adapted;

import org.duckapter.AdaptedClass;

abstract class AbstractAdaptedClass<O,D> implements AdaptedClass<O,D> {

	protected boolean canAdaptClass = true;
	protected boolean canAdaptInstance = true;
	private final Class<D> duckInterface;
	private final Class<O> originalClass;



	public AbstractAdaptedClass(final Class<D> duckInterface, final Class<O> originalClass) {
		this.duckInterface = duckInterface;
		this.originalClass = originalClass;
	}

	@Override
	public boolean canAdaptClass() {
		return canAdaptClass;
	}

	@Override
	public boolean canAdaptInstance() {
		return canAdaptInstance;
	}

	@Override
	public Class<D> getDuckInterface() {
		return duckInterface;
	}

	@Override
	public Class<O> getOriginalClass() {
		return originalClass;
	}

}