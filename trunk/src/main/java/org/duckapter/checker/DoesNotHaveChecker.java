package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.DoesNotHaveAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.DoesNotHave;

public class DoesNotHaveChecker extends AbstractChecker<DoesNotHave> {

	@Override
	public InvocationAdapter adapt(DoesNotHave anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			return new DoesNotHaveAdapter(duckMethod.getReturnType());
		}
		return InvocationAdapters.NULL;
	}
	
}
