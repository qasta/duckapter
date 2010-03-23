package org.duckapter.adapted;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.duckapter.AdaptationViolation;
import org.duckapter.AdaptedClass;
import org.duckapter.Checker;

final class EmptyAdaptedClass implements AdaptedClass {

	private final Collection<AdaptationViolation> adaptationViolations = new ArrayList<AdaptationViolation>();

	public static interface InterfaceChecker<T extends Annotation> extends Checker<T>{}
	
	private final Class<?> originalClass;
	private final Class<?> duckInterface;

	EmptyAdaptedClass(Class<?> originalClass, Class<?> duckInterface) {
		this.originalClass = originalClass;
		this.duckInterface = duckInterface;
		for (Method m : duckInterface.getMethods()) {
			adaptationViolations.add(new AdaptationViolation(m, duckInterface, InterfaceChecker.class));
		}
	}

	@Override
	public boolean canAdaptClass() {
		return false;
	}

	@Override
	public boolean canAdaptInstance() {
		return false;
	}

	@Override
	public Class<?> getDuckInterface() {
		return duckInterface;
	}

	@Override
	public Class<?> getOriginalClass() {
		return originalClass;
	}

	@Override
	public Collection<AdaptationViolation> getAdaptationViolations() {
		return new ArrayList<AdaptationViolation>(adaptationViolations);
	}

	@Override
	public Object invoke(Object originalInstance, Method duckMethod,
			Object[] args) throws Throwable {
		throw new UnsupportedOperationException("Cannot invoke method on empty adapted class!");
	}

}
