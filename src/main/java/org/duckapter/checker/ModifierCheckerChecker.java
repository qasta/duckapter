package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.ModifierChecker;

public class ModifierCheckerChecker extends AbstractChecker<Annotation> {

	private boolean checkModifiers(Annotation f, final int modifiers) {
		return (modifiers & getMask(f)) != 0;
	}

	@Override
	public final InvocationAdapter adapt(Annotation anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (checkModifiers(anno, Checkers.getModifiers(original))) {
			return InvocationAdapters.OK;
		}
		return InvocationAdapters.NULL;
	}

	protected int getMask(Annotation fin) {
		if (fin instanceof ModifierChecker) {
			return ((ModifierChecker) fin).value();
		}
		if (fin.annotationType().isAnnotationPresent(ModifierChecker.class)) {
			return fin.annotationType().getAnnotation(ModifierChecker.class)
					.value();
		}
		throw new IllegalStateException("Annotation " + fin
				+ " declares modifier checker but is "
				+ "not annotated by @ModifierChecker annotation!");
	}
}
