package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import org.duckapter.Checker;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.adapter.OptionalAdapter;
import org.duckapter.annotation.Optional;

public class OptionalChecker implements Checker<Optional> {

	@SuppressWarnings("unchecked")
	@Override
	public boolean canAdapt(Optional anno, AnnotatedElement element) {
		return !(element instanceof Class);
	}
	
	@Override
	public MethodAdapter adapt(Optional anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (!(duck instanceof Method)) {
			return MethodAdapters.NULL;
		}
		return new OptionalAdapter(((Method)duck).getReturnType());
	}
	
	@Override
	public <A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			Optional anno, AnnotatedElement element) {
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
