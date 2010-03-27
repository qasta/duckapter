package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.annotation.Stereotype;
import org.duckapter.annotation.StereotypeType;

public class StereotypeChecker implements Checker<Annotation> {

	@Override
	public InvocationAdapter adapt(Annotation anno, AnnotatedElement original,
			AnnotatedElement duck) {
		return getStereotypeType(anno).adapt(anno, original, duck,
				getCheckers(anno));
	}

	@Override
	public boolean canAdapt(Annotation anno, AnnotatedElement element) {
		return getStereotypeType(anno).canAdapt(anno, element,
				getCheckers(anno));
	}

	private Map<Checker<Annotation>, Annotation> getCheckers(Annotation anno) {
		final Map<Checker<Annotation>, Annotation> checkers = Checkers.collectCheckers(anno.annotationType());
		checkers.keySet().removeAll(Checkers.getDefaultCheckers());
		return checkers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			Annotation anno, AnnotatedElement element) {
		Collection ret = new HashSet();
		for (Entry<Checker<Annotation>, Annotation> entry : getCheckers(anno).entrySet()) {
			ret.addAll(entry.getKey().suppressCheckers(entry.getValue(), element));
		}
		return Collections.unmodifiableCollection(ret);
	}

	private StereotypeType getStereotypeType(Annotation anno) {
		if (anno instanceof Stereotype) {
			return ((Stereotype) anno).value();
		}
		if (anno.annotationType().isAnnotationPresent(Stereotype.class)) {
			return anno.annotationType().getAnnotation(Stereotype.class)
					.value();
		}
		throw new IllegalStateException("Annotation " + anno
				+ " declares stereotype checker but is "
				+ "not annotated by @Sterotype annotation!");
	}

	@Override
	public boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}

	private static final int HASH = Checkers.hashCode(StereotypeChecker.class);

	@Override
	public int hashCode() {
		return HASH;
	}

}
