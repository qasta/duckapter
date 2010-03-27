package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.ModifierMask;

public class ModifierChecker extends AbstractChecker<Annotation> {

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
		if (fin instanceof ModifierMask) {
			return ((ModifierMask) fin).value();
		}
		if (fin.annotationType().isAnnotationPresent(ModifierMask.class)) {
			return fin.annotationType().getAnnotation(ModifierMask.class)
					.value();
		}
		throw new IllegalStateException("Annotation " + fin
				+ " declares modifier checker but is "
				+ "not annotated by @ModifierMask annotation!");
	}
}
