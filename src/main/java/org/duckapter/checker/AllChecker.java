package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.AllMethodAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.All;

public class AllChecker implements Checker<All> {

	@SuppressWarnings("unchecked")
	private static final List<Class<? extends BooleanCheckerBase>> SUPPRESSED = Arrays
			.asList(ReturnTypeChecker.class, ParametersChecker.class,
					ExceptionsChecker.class);
	@SuppressWarnings("unchecked")
	private static final Collection<BooleanCheckerBase> SUPPRESSED_CHECKERS;

	static {
		@SuppressWarnings("unchecked")
		Collection<BooleanCheckerBase> checkers = new ArrayList<BooleanCheckerBase>();
		for (@SuppressWarnings("unchecked")
		Class<? extends BooleanCheckerBase> checkerClass : SUPPRESSED) {
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
	public InvocationAdapter adapt(All anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (original instanceof Class) {
			return InvocationAdapters.OK;
		}
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			return checkReturnType(anno, original, duckMethod.getReturnType());

		}
		return InvocationAdapters.NULL;
	}

	private InvocationAdapter checkReturnType(All anno, AnnotatedElement original, Class<?> retType) {
		if (!retType.isArray()) {
			return InvocationAdapters.NULL;
		}
		return checkMethodsType(anno, original, (Class<?>) retType.getComponentType());
	}

	private InvocationAdapter checkMethodsType(All anno, AnnotatedElement original,
			Class<?> methodsType) {
		if (!methodsType.isInterface() || methodsType.getMethods().length != 1) {
			return InvocationAdapters.NULL;
		}
		
		return checkMethodInterface(anno, original, methodsType.getMethods()[0]);
	}

	private InvocationAdapter checkMethodInterface(All anno, AnnotatedElement original,
			final Method returnTypeOnlyMethod) {
		for (BooleanCheckerBase<Annotation> ch : SUPPRESSED_CHECKERS) {
			if (ch.canAdapt(anno, original)
					&& InvocationAdapters.isNull(ch.adapt(anno, original, returnTypeOnlyMethod))) {
				return InvocationAdapters.NULL;
			}
		}
		return new AllMethodAdapter(original, returnTypeOnlyMethod);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canAdapt(All anno, AnnotatedElement element) {
		if (element instanceof Class) {
			return false;
		}
		for (BooleanCheckerBase<Annotation> ch : SUPPRESSED_CHECKERS) {
			if (ch.canAdapt(anno, element)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Class<? extends BooleanCheckerBase>> suppressCheckers(
			All anno, AnnotatedElement duckMethod) {
		return SUPPRESSED;
	}
	
	@Override
	public boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}
	
	private static final int HASH = Checkers.hashCode(AllChecker.class);

	@Override
	public int hashCode() {
		return HASH;
	}

}
