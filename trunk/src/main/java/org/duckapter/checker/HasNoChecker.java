package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.HasNoAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.HasNo;

public class HasNoChecker extends AbstractChecker<HasNo> {

	@Override
	public InvocationAdapter adapt(HasNo anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			return new HasNoAdapter(duckMethod.getReturnType());
		}
		return InvocationAdapters.NULL;
	}
	
}
