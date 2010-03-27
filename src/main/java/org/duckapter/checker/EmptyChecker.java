package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Collections;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;

public class EmptyChecker<T extends Annotation> implements Checker<T> {

	private static final EmptyChecker<Annotation> INSTANCE = new EmptyChecker<Annotation>();
	
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> Checker<A> getInstance(){
		return (EmptyChecker<A>) INSTANCE;
	}
	
	@Override
	public InvocationAdapter adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		return InvocationAdapters.OK;
	}

	@Override
	public boolean canAdapt(T anno, AnnotatedElement element) {
		return false;
	}

	@Override
	public <A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			T anno, AnnotatedElement element) {
		return Collections.emptyList();
	}
	
	@Override
	public boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return Checkers.hashCode(this);
	}

}
