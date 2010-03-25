package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.AllMethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.annotation.All;

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
	public MethodAdapter check(All anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (original instanceof Class) {
			return MethodAdapters.OK;
		}
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			return checkReturnType(anno, original, duckMethod.getReturnType());

		}
		return MethodAdapters.NULL;
	}

	private MethodAdapter checkReturnType(All anno, AnnotatedElement original, Class<?> retType) {
		if (!retType.isArray()) {
			return MethodAdapters.NULL;
		}
		return checkMethodsType(anno, original, (Class<?>) retType.getComponentType());
	}

	private MethodAdapter checkMethodsType(All anno, AnnotatedElement original,
			Class<?> methodsType) {
		if (!methodsType.isInterface() || methodsType.getMethods().length != 1) {
			return MethodAdapters.NULL;
		}
		
		return checkMethodInterface(anno, original, methodsType.getMethods()[0]);
	}

	private MethodAdapter checkMethodInterface(All anno, AnnotatedElement original,
			final Method returnTypeOnlyMethod) {
		for (DefaultChecker<Annotation> ch : SUPPRESSED_CHECKERS) {
			if (ch.doesCheck(anno, original)
					&& MethodAdapters.isNull(ch.check(anno, original, returnTypeOnlyMethod))) {
				return MethodAdapters.NULL;
			}
		}
		return new AllMethodAdapter(original, returnTypeOnlyMethod);
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
