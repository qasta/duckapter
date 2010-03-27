package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.duckapter.Checker;
import org.duckapter.CheckerAnnotation;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;

public class AnnotationsChecker<T extends Annotation> implements Checker<T> {

	@Override
	public boolean canAdapt(T t, AnnotatedElement element) {
		return true;
	};

	public InvocationAdapter adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		Collection<Annotation> fromDuck = collectAnnotations(duck);
		Collection<Annotation> fromOriginal = collectAnnotations(original);
		if (fromDuck.equals(fromOriginal)) {
			return InvocationAdapters.OK;
		}
		return InvocationAdapters.NULL;
	}

	private Collection<Annotation> collectAnnotations(AnnotatedElement duck) {
		Collection<Annotation> fromDuck = new HashSet<Annotation>();
		for (Annotation a : duck.getAnnotations()) {
			CheckerAnnotation dann = Checkers.getCheckerAnnotation(a);
			if (dann == null) {
				fromDuck.add(a);
			}
		}
		return fromDuck;
	};

	@SuppressWarnings("unchecked")
	@Override
	public <A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			T anno, AnnotatedElement element) {
		if (hasRelevantAnnotations(element)) {
			Collection<Class<Ch>> col = new ArrayList<Class<Ch>>();
			col.add((Class<Ch>) NameChecker.class);
			return Collections.unmodifiableCollection(col);
		}
		return Collections.emptyList();
	};

	private boolean hasRelevantAnnotations(AnnotatedElement element) {
		for (Annotation anno : element.getAnnotations()) {
			if (Checkers.getCheckerAnnotation(anno) == null) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}
	
	private static final int HASH = Checkers.hashCode(AnnotationsChecker.class);

	@Override
	public int hashCode() {
		return HASH;
	}

}
