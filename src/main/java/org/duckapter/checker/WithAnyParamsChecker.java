package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.WithAnyParams;

/**
 * Checker for the {@link WithAnyParams} annotation. Suppresses
 * {@link ParametersChecker}.
 * 
 * @author Vladimir Orany
 * @see WithAnyParams
 */
public class WithAnyParamsChecker extends AbstractChecker<WithAnyParams> {

	public org.duckapter.InvocationAdapter adapt(WithAnyParams anno,
			AnnotatedElement original, AnnotatedElement duck,
			Class<?> classOfOriginal) {
		return InvocationAdapters.DISCRIMINATOR;
	};

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<ParametersChecker>> suppressCheckers(WithAnyParams anno,
			AnnotatedElement element) {
		return Arrays.asList(ParametersChecker.class);
	}

}
