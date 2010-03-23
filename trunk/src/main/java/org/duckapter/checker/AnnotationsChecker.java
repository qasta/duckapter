package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.duckapter.Checker;
import org.duckapter.DuckAnnotation;
import org.duckapter.adapted.MethodAdapter;
import org.duckapter.adapted.MethodAdapters;

public class AnnotationsChecker<T extends Annotation> implements Checker<T> {

	@Override
	public boolean doesCheck(T t, AnnotatedElement element) {
		return true;
	};

	public MethodAdapter check(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		Collection<Annotation> fromDuck = collectAnnotations(duck);
		Collection<Annotation> fromOriginal = collectAnnotations(original);
		if (fromDuck.equals(fromOriginal)) {
			return MethodAdapters.OK;
		}
		return MethodAdapters.NULL;
	}

	private Collection<Annotation> collectAnnotations(AnnotatedElement duck) {
		Collection<Annotation> fromDuck = new HashSet<Annotation>();
		for (Annotation a : duck.getAnnotations()) {
			if (!a.annotationType().isAnnotationPresent(DuckAnnotation.class)) {
				fromDuck.add(a);
			}
		}
		return fromDuck;
	};

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<NameChecker>> suppressCheckers(AnnotatedElement duckMethod) {
		if (hasRelevantAnnotations(duckMethod)) {
			return Arrays.asList(NameChecker.class);
		}
		return Collections.emptyList();
	}

	private boolean hasRelevantAnnotations(AnnotatedElement element) {
		for (Annotation anno : element.getAnnotations()) {
			if (!anno.annotationType()
					.isAnnotationPresent(DuckAnnotation.class)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return obj.getClass().equals(getClass());
	}

	@Override
	public int hashCode() {
		return 37 + 37 * getClass().getName().hashCode();
	}

}
