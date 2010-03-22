/**
 * 
 */
package org.duckapter;

import java.lang.reflect.AnnotatedElement;


public class AdaptationViolation {
	private final AnnotatedElement duck;
	private final AnnotatedElement element;
	@SuppressWarnings("unchecked")
	private final Class<? extends Checker> checker;

	@SuppressWarnings("unchecked")
	public AdaptationViolation(AnnotatedElement duck,
			AnnotatedElement element, Class<? extends Checker> checker) {
		this.duck = duck;
		this.element = element;
		this.checker = checker;
	}

	public AnnotatedElement getDuck() {
		return duck;
	}

	public AnnotatedElement getElement() {
		return element;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Checker> getChecker() {
		return checker;
	}
	
	@Override
	public String toString() {
		return String.format("Checker %s returned false when checking duck method %s on %s!", checker, duck, element);
	}

}