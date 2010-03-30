package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.Declared;

public class DeclaredChecker extends AbstractChecker<Declared> {

	@Override
	public InvocationAdapter adapt(Declared anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal) {
		if (original instanceof Member && ((Member) original).getDeclaringClass().equals(classOfOriginal)) {
			return InvocationAdapters.OK;
		}
		return InvocationAdapters.NULL;
	}
	
}
