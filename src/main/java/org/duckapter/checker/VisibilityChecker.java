package org.duckapter.checker;

import static org.duckapter.checker.Checkers.getModifiers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

import org.duckapter.Checker;
import org.duckapter.Duck;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.annotation.Visibility;


public class VisibilityChecker implements Checker<Annotation> {

	private static interface VisibilityAnno {
		Visibility value();
	}

	@Override
	public final boolean canAdapt(Annotation anno, AnnotatedElement element) {
		return true;
	}

	@Override
	public final MethodAdapter adapt(Annotation anno, AnnotatedElement original,
			AnnotatedElement duck) {
		return getVisibility(anno).check(anno, getModifiers(original)) 
				? MethodAdapters.OK
				: MethodAdapters.NULL;
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
		return Arrays.asList(PublicOnlyChecker.class);
	}
	
	@Override
	public boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}

	private static final int HASH = Checkers.hashCode(VisibilityChecker.class);

	@Override
	public int hashCode() {
		return HASH;
	}
}
