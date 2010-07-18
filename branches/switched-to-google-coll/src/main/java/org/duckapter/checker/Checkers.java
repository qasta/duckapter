package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.duckapter.Checker;

import com.google.common.collect.ImmutableList;

/**
 * Utility class for checkers.
 * 
 * @author Vladimir Orany
 * 
 */
public final class Checkers {

	private Checkers() {
		// prevents instance creation and subtyping
	}

	@SuppressWarnings("unchecked")
	private static final Collection<? extends Checker> defaultCheckers = ImmutableList.of(
			new ExceptionsChecker<Annotation>(), new NameChecker<Annotation>(),
			new MethodsOnlyChecker<Annotation>(), new PublicOnlyChecker<Annotation>(),
			new ConcreteMethodsChecker<Annotation>(),
			new ParametersChecker<Annotation>(),
			new ReturnTypeChecker<Annotation>());

	/**
	 * Return the collection of the default checkers which are always used until
	 * suppressed by
	 * {@link Checker#suppressCheckers(Annotation, AnnotatedElement)} method.
	 * The default checker are listed under the see tag.
	 * 
	 * @param <T>
	 *            common checker annotation of all default checkers
	 * @return the collection of all default checkers
	 * @see AnnotatedWithChecker
	 * @see ExceptionsChecker
	 * @see NameChecker
	 * @see MethodsOnlyChecker
	 * @see PublicOnlyChecker
	 * @see ConcreteMethodsChecker
	 * @see ParametersChecker
	 * @see ReturnTypeChecker
	 */
	@SuppressWarnings("unchecked")
	public static final Collection<Checker> getDefaultCheckers() {
		return (Collection<Checker>) defaultCheckers;
	}








	/**
	 * Collect instances of checkers using annotation placed on the element and
	 * the defualt checkers.
	 * 
	 * @param <T>
	 *            the common annotation type
	 * @param m
	 *            the annotated element
	 * @return the collection of checkers for specified element
	 */
	@SuppressWarnings("unchecked")
	public static Map<Checker<Annotation>, Annotation> collectCheckers(
			AnnotatedElement m) {
		Map<Checker<Annotation>, Annotation> methodCheckers = new LinkedHashMap<Checker<Annotation>, Annotation>();
		for (Annotation anno : m.getAnnotations()) {
			CheckerDescriptor desc = CheckerDescriptor.getDescriptor(anno);
			if (desc.isValid()) {
				methodCheckers.put(desc.getChecker(), anno);
			}
		}
		for (Checker<Annotation> ch : getDefaultCheckers()) {
			methodCheckers.put(ch, null);
		}
		Set<Entry<Checker<Annotation>, Annotation>> entries = (Set<Entry<Checker<Annotation>, Annotation>>) methodCheckers
				.entrySet();
		Set<Checker<Annotation>> suppressedMethodChecker = new HashSet<Checker<Annotation>>();
		for (Entry<Checker<Annotation>, Annotation> e : entries) {
			for (Class<? extends Checker> clazz : suppressCheckers(e.getValue())) {
				CheckerDescriptor descriptor = CheckerDescriptor.getDescriptor(clazz);
				suppressedMethodChecker.add(descriptor.getChecker());
			}
		}
		methodCheckers.keySet().removeAll(suppressedMethodChecker);
		return Collections.unmodifiableMap(methodCheckers);
	}

	/**
	 * Returns modifiers for specified element.
	 * 
	 * @param original
	 *            the element
	 * @return modifiers for the specified element
	 */
	public static int getModifiers(AnnotatedElement original) {
		if (original instanceof Class<?>) {
			return ((Class<?>) original).getModifiers();
		}
		if (original instanceof Member) {
			return ((Member) original).getModifiers();
		}
		throw new IllegalArgumentException("Illegal argument: " + original);
	}

	/**
	 * Optimized method for the checkers.
	 * 
	 * @param ch
	 *            the checker
	 * @param obj
	 *            the other object
	 * @return if the checker equals the other object
	 */
	public static boolean equals(Checker<?> ch, Object obj) {
		if (obj == null) {
			return false;
		}
		if (ch == obj) {
			return true;
		}
		return ch.hashCode() == obj.hashCode();
	}
	

	/**
	 * TODO
	 * 
	 * @param checkers
	 * @return
	 */
	public static int getMinPriorityToFail(
			Map<Checker<Annotation>, Annotation> checkers) {
		int ret = Integer.MAX_VALUE;
		for (Entry<Checker<Annotation>, Annotation> e : checkers.entrySet()) {
			final int min = getMinAdapterPriorityToFail(e.getValue());
			if (min < ret) {
				ret = min;
			}
		}
		return ret;
	}

	public static int getMinPriorityToPass(
			Map<Checker<Annotation>, Annotation> checkers) {
		int ret = Integer.MIN_VALUE;
		for (Entry<Checker<Annotation>, Annotation> e : checkers.entrySet()) {
			final int max = getMinAdapterPriorityToPass(e.getValue());
			if (max > ret) {
				ret = max;
			}
		}
		return ret;
	}


	
	/**
	 * Return hash
	 * 
	 * @param clazz
	 * @return
	 */
	public static int hashCode(Class<?> clazz) {
		return 37 + 37 * clazz.getName().hashCode();
	}

	/**
	 * Return hash code for the checker based on common convention.
	 * 
	 * @param ch
	 *            the checker
	 * @return the hash code for the checker
	 */
	public static int hashCode(Checker<?> ch) {
		return 37 + 37 * ch.getClass().getName().hashCode();
	}

	@SuppressWarnings("unchecked")
	public static Collection<Class<? extends Checker>> suppressCheckers(
			Annotation anno) {
		return CheckerDescriptor.getDescriptor(anno).getSuppressed();
	}

	public static int getMinAdapterPriorityToFail(Annotation anno) {
		return CheckerDescriptor.getDescriptor(anno).getMinToFail();
	}
	
	public static int getMinAdapterPriorityToPass(Annotation anno) {
		return CheckerDescriptor.getDescriptor(anno).getMinToPass();
	}
	
	public static boolean canAdapt(Annotation anno, AnnotatedElement element) {
		return CheckerDescriptor.getDescriptor(anno).canAdapt(element);
	}

}
