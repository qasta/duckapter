package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;

public class AnnotationsChecker<T extends Annotation> extends
		AbstractChecker<T> {

	protected Collection<ElementType> getTargetElements(T anno) {
		return ALL_TARGETS;
	}

	public InvocationAdapter adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		Collection<Annotation> fromDuck = collectAnnotations(duck);
		Collection<Annotation> fromOriginal = collectAnnotations(original);
		if (fromDuck.equals(fromOriginal)) {
			return InvocationAdapters.OK;
		}
		return InvocationAdapters.NULL;
	}

	private Collection<Annotation> collectAnnotations(AnnotatedElement duck) {
		Collection<Annotation> fromDuck = new ArrayList<Annotation>();
		for (Annotation a : duck.getAnnotations()) {
			if (!Checkers.isCheckerAnnotation(a)) {
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
			return (Collection<Class<Ch>>)SUPPRESSED;
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private static final Collection SUPPRESSED = getSuppressed();
	
	@SuppressWarnings("unchecked")
	private static <Ch> Collection<Class<Ch>> getSuppressed() {
		Collection<Class<Ch>> col = new ArrayList<Class<Ch>>();
		col.add((Class<Ch>) NameChecker.class);
		return Collections.unmodifiableCollection(col);
	};

	private boolean hasRelevantAnnotations(AnnotatedElement element) {
		for (Annotation anno : element.getAnnotations()) {
			if (!Checkers.isCheckerAnnotation(anno)) {
				return true;
			}
		}
		return false;
	}

}
