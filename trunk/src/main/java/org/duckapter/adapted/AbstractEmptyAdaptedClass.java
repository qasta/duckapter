package org.duckapter.adapted;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


abstract class AbstractEmptyAdaptedClass<O,D> extends AbstractAdaptedClass<O,D> {

	protected final Collection<Method> unimplMethods = new ArrayList<Method>();

	AbstractEmptyAdaptedClass(Class<D> duckInterface,
			Class<O> originalClass, boolean canAdapt) {
		super(duckInterface, originalClass);
		canAdaptClass = canAdapt;
		canAdaptInstance = canAdapt;
		for (Method m : duckInterface.getMethods()) {
			unimplMethods.add(m);
		}
	}

	@Override
	public Object invoke(Object originalInstance, Method duckMethod,
			Object[] args) throws Throwable {
		throw new UnsupportedOperationException(
				"Cannot invoke method on empty adapted class!");
	}

	@Override
	public Collection<Method> getUnimplementedForClass() {
		return Collections.unmodifiableCollection(unimplMethods);
	}

	@Override
	public Collection<Method> getUnimplementedForInstance() {
		return Collections.unmodifiableCollection(unimplMethods);
	}

}