package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.annotation.StereotypeChecker;

/**
 * Checker for the {@link StereotypeChecker} metaannotation.
 * 
 * @author Vladimir Orany
 * @see StereotypeChecker
 */
public class StereotypeCheckerChecker implements Checker<Annotation> {

	public InvocationAdapter adapt(Annotation anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		return getStereotypeType(anno).adapt(anno, original, duck,
				classOfOriginal, getCheckers(anno));
	}
	
	public boolean canAdapt(Annotation anno, AnnotatedElement element,
			Class<?> classOfOriginal) {
		return getStereotypeType(anno).canAdapt(anno, element, classOfOriginal,
				getCheckers(anno));
	}

	private static final Map<Annotation, Map<Checker<Annotation>, Annotation>> checkersCache = new HashMap<Annotation, Map<Checker<Annotation>, Annotation>>();

	private Map<Checker<Annotation>, Annotation> getCheckers(Annotation anno) {
		if (checkersCache.containsKey(anno)) {
			return checkersCache.get(anno);
		}
		final Map<Checker<Annotation>, Annotation> checkers = new HashMap<Checker<Annotation>, Annotation>(
				Checkers.collectCheckers(anno.annotationType()));
		checkers.keySet().removeAll(Checkers.getDefaultCheckers());
		checkersCache.put(anno, checkers);
		return checkers;
	}

	private StereotypeType getStereotypeType(Annotation anno) {
		if (anno instanceof StereotypeChecker) {
			return ((StereotypeChecker) anno).value();
		}
		if (anno.annotationType().isAnnotationPresent(StereotypeChecker.class)) {
			return anno.annotationType().getAnnotation(StereotypeChecker.class)
					.value();
		}
		throw new IllegalStateException("Annotation " + anno
				+ " declares stereotype checker but is "
				+ "not annotated by @SterotypeChecker annotation!");
	}

	@Override
	public boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}

	private static final int HASH = Checkers
			.hashCode(StereotypeCheckerChecker.class);

	@Override
	public int hashCode() {
		return HASH;
	}

}
