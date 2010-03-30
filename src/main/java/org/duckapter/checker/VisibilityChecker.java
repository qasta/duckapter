package org.duckapter.checker;

import static org.duckapter.checker.Checkers.getModifiers;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.duckapter.Duck;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.Visibility;


public class VisibilityChecker extends AbstractChecker<Annotation> {

	@SuppressWarnings("unchecked")
	private static final List<Class<PublicOnlyChecker>> SUPPRESSED = Arrays.asList(PublicOnlyChecker.class);

	private static interface VisibilityAnno {
		Visibility value();
	}

	@Override
	protected Collection<ElementType> getTargetElements(Annotation anno) {
		return ALL_TARGETS;
	}

	@Override
	public final InvocationAdapter adapt(Annotation anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		return getVisibility(anno).check(anno, getModifiers(original)) 
				? InvocationAdapters.OK
				: InvocationAdapters.NULL;
	}


	protected Visibility getVisibility(Annotation anno) {
		if (!Duck.test(anno, VisibilityAnno.class)) {
			throw new IllegalArgumentException("Cannot adapt annotation: "
					+ anno);
		}
		return Duck.type(anno, VisibilityAnno.class).value();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<PublicOnlyChecker>> suppressCheckers(Annotation anno,
			AnnotatedElement duckMethod) {
		return SUPPRESSED;
	}
	
}
