package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.adapter.OptionalAdapter;
import org.duckapter.annotation.Optional;

public class OptionalChecker extends AbstractChecker<Optional> {

	@Override
	public InvocationAdapter adapt(Optional anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		if (!(duck instanceof Method)) {
			return InvocationAdapters.NULL;
		}
		return new OptionalAdapter(((Method) duck).getReturnType());
	}

}
