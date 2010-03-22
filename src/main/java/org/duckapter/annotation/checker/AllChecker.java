package org.duckapter.annotation.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.duckapter.annotation.All;
import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.ExceptionsChecker;
import org.duckapter.checker.ParametersChecker;
import org.duckapter.checker.ReturnTypeChecker;

public class AllChecker extends DefaultChecker<All> {

	@SuppressWarnings("unchecked")
	private static final List<Class<? extends DefaultChecker>> SUPPRESSED = Arrays
			.asList(ReturnTypeChecker.class, ParametersChecker.class,
					ExceptionsChecker.class);
	@SuppressWarnings("unchecked")
	private static final Collection<DefaultChecker> SUPPRESSED_CHECKERS;

	static {
		@SuppressWarnings("unchecked")
		Collection<DefaultChecker> checkers = new ArrayList<DefaultChecker>();
		for (@SuppressWarnings("unchecked")
		Class<? extends DefaultChecker> checkerClass : SUPPRESSED) {
			try {
				checkers.add(checkerClass.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		SUPPRESSED_CHECKERS = Collections.unmodifiableCollection(checkers);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean check(All anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (original instanceof Class) {
			return true;
		}
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			return checkReturnType(anno, original, duckMethod.getReturnType());

		}
		return true;
	}

	private boolean checkReturnType(All anno, AnnotatedElement original, Class<?> retType) {
		if (!retType.isArray()) {
			return false;
		}
		return checkMethodsType(anno, original, (Class<?>) retType.getComponentType());
	}

	private boolean checkMethodsType(All anno, AnnotatedElement original,
			Class<?> methodsType) {
		if (!methodsType.isInterface() || methodsType.getMethods().length != 1) {
			return false;
		}
		
		return checkMethodInterface(anno, original, methodsType.getMethods()[0]);
	}

	private boolean checkMethodInterface(All anno, AnnotatedElement original,
			final Method returnTypeOnlyMethod) {
		for (DefaultChecker<Annotation> ch : SUPPRESSED_CHECKERS) {
			if (ch.doesCheck(anno, original)
					&& !ch.check(anno, original, returnTypeOnlyMethod)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean doesCheck(All anno, AnnotatedElement element) {
		if (element instanceof Class) {
			return false;
		}
		for (DefaultChecker<Annotation> ch : SUPPRESSED_CHECKERS) {
			if (ch.doesCheck(anno, element)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Class<? extends DefaultChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return SUPPRESSED;
	}

}
