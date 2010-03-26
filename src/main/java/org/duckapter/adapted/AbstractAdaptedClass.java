package org.duckapter.adapted;

import org.duckapter.AdaptedClass;

abstract class AbstractAdaptedClass implements AdaptedClass {

	protected boolean canAdaptClass;
	protected boolean canAdaptInstance;
	protected final Class<?> duckInterface;
	protected final Class<?> originalClass;



	public AbstractAdaptedClass(final Class<?> duckInterface, final Class<?> originalClass) {
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
	public Class<?> getDuckInterface() {
		return duckInterface;
	}

	@Override
	public Class<?> getOriginalClass() {
		return originalClass;
	}

}