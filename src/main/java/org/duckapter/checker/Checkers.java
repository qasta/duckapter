package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.duckapter.Checker;
import org.duckapter.CheckerAnnotation;
import org.duckapter.Duck;
import org.duckapter.adapter.InvocationAdaptersPriorities;
import org.duckapter.annotation.CanCheck;
import org.duckapter.annotation.MinToFail;
import org.duckapter.annotation.MinToPass;
import org.duckapter.annotation.SuppressChecker;

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

	private static Map<Class<?>, Checker<?>> checkerClassToInstanceMap = new HashMap<Class<?>, Checker<?>>();

	/**
	 * Return shared instance for the specified checker class.
	 * 
	 * @param <A>
	 *            the checker's annotation
	 * @param theClass
	 *            the class of the checker
	 * @return the instance of specified checker
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> Checker<A> getCheckerInstance(
			Class<? extends Checker<? extends Annotation>> theClass) {
		Checker<A> checker = (Checker<A>) checkerClassToInstanceMap
				.get(theClass);
		if (checker == null) {
			try {
				checker = (Checker<A>) theClass.getConstructor().newInstance();
				checkerClassToInstanceMap.put(theClass, checker);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return checker;
	}

	/**
	 * Tests the annotation for being the checker annotation.
	 * 
	 * @param anno
	 *            the annotation which might be the checker annotation
	 * @return <code>true</code> if the annotation is the checker annotation
	 */
	public static boolean isCheckerAnnotation(Annotation anno) {
		return !EmptyChecker.getInstance().equals(getChecker(anno));
	}

	@SuppressWarnings("unchecked")
	private static CheckerAnnotation getCheckerAnnotation(Annotation anno) {
		return getCheckerAnno(anno.annotationType(), Target.class,
				Retention.class, Documented.class, Inherited.class);
	}

	private static CheckerAnnotation getCheckerAnno(
			Class<? extends Annotation> anno,
			Class<? extends Annotation>... exclude) {
		if (anno.isAnnotationPresent(CheckerAnnotation.class)) {
			return anno.getAnnotation(CheckerAnnotation.class);
		}
		Collection<Class<? extends Annotation>> toTest = new ArrayList<Class<? extends Annotation>>();
		final List<Class<? extends Annotation>> excludeList = Arrays
				.asList(exclude);
		final List<Annotation> withCheckerAnnotation = new ArrayList<Annotation>();
		for (Annotation a : anno.getAnnotations()) {
			if (excludeList.contains(a)) {
				continue;
			}
			if (a.annotationType().isAnnotationPresent(CheckerAnnotation.class)) {
				withCheckerAnnotation.add(a);
			} else {
				for (Annotation annoToTest : a.annotationType()
						.getAnnotations()) {
					toTest.add(annoToTest.annotationType());
				}
				toTest.remove(a);
			}
		}
		if (!withCheckerAnnotation.isEmpty()) {
			return getWithHighestPriority(withCheckerAnnotation);
		}
		toTest.remove(anno);
		toTest.removeAll(excludeList);
		return testForCheckerAnno(toTest, excludeList);
	}

	@SuppressWarnings("unchecked")
	private static CheckerAnnotation testForCheckerAnno(
			Collection<Class<? extends Annotation>> toTest,
			final List<Class<? extends Annotation>> excludeList) {
		for (Class<? extends Annotation> a : toTest) {
			CheckerAnnotation dann = getCheckerAnno(a, (Class[]) excludeList
					.toArray(new Class[excludeList.size()]));
			if (dann != null) {
				return dann;
			}
		}
		return null;
	}

	private static CheckerAnnotation getWithHighestPriority(
			final List<Annotation> withCheckerAnnotation) {
		Annotation toReturn = withCheckerAnnotation.get(0);
		for (int i = 1; i < withCheckerAnnotation.size(); i++) {
			if (getAnnoPriority(toReturn) < getAnnoPriority(withCheckerAnnotation
					.get(i))) {
				toReturn = withCheckerAnnotation.get(i);
			}
		}
		return toReturn.annotationType().getAnnotation(CheckerAnnotation.class);
	}

	private static interface CheckerWithPriority {
		int checkerPriority();
	}

	private static int getAnnoPriority(Annotation toReturn) {
		if (Duck.test(toReturn, CheckerWithPriority.class)) {
			return Duck.type(toReturn, CheckerWithPriority.class)
					.checkerPriority();
		}
		return Integer.MIN_VALUE;
	}

	private static final Map<Class<? extends Annotation>, Checker<? extends Annotation>> checkersCache = new HashMap<Class<? extends Annotation>, Checker<? extends Annotation>>();

	/**
	 * Returns an instance of checker bind to specified annotation.
	 * 
	 * @param <A>
	 *            the type of the annotation
	 * @param anno
	 *            the annotation
	 * @return the checker bind to this annotation
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> Checker<A> getChecker(A anno) {
		final Class<? extends Annotation> annotationType = anno
				.annotationType();
		Checker<A> checker = (Checker<A>) checkersCache.get(annotationType);
		if (checker != null) {
			return checker;
		}
		checker = createChecker(anno);
		checkersCache.put(annotationType, (Checker<Annotation>) checker);
		return checker;
	}

	private static <A extends Annotation> Checker<A> createChecker(A anno) {
		final CheckerAnnotation checkerAnnotation = getCheckerAnnotation(anno);
		if (checkerAnnotation == null) {
			return EmptyChecker.getInstance();
		}
		Checker<A> ch = getCheckerInstance(checkerAnnotation.value());
		return ch;
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
			if (isCheckerAnnotation(anno)) {
				methodCheckers.put(getChecker(anno), anno);
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
				suppressedMethodChecker
						.add(getCheckerInstance((Class<? extends Checker<? extends Annotation>>) clazz));
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

	public static int getMinAdapterPriorityToFail(Annotation anno) {
		return getDescriptor(anno).getMinToFail();
	}

	private static int getMinAdapterPriorityToFail(
			Class<? extends Annotation> annotationType) {
		if (!annotationType.isAnnotationPresent(MinToFail.class)) {
			return InvocationAdaptersPriorities.NONE;
		}
		return annotationType.getAnnotation(MinToFail.class).value();
	}

	public static int getMinAdapterPriorityToPass(Annotation anno) {
		return getDescriptor(anno).getMinToPass();
	}

	private static int getMinAdapterPriorityToPass(
			Class<? extends Annotation> annotationType) {
		if (!annotationType.isAnnotationPresent(MinToPass.class)) {
			return InvocationAdaptersPriorities.METHOD;
		}
		return annotationType.getAnnotation(MinToPass.class).value();
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

	/**
	 * Return hash
	 * 
	 * @param clazz
	 * @return
	 */
	public static int hashCode(Class<?> clazz) {
		return 37 + 37 * clazz.getName().hashCode();
	}

	@SuppressWarnings("unchecked")
	public static Collection<Class<? extends Checker>> suppressCheckers(
			Annotation anno) {
		return getDescriptor(anno).getSuppressed();
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends Checker>[] suppressCheckers(
			final Class<? extends Annotation> annotationType) {
		if (!annotationType.isAnnotationPresent(SuppressChecker.class)) {
			return (Class<? extends Checker>[]) new Class[0];
		}
		return annotationType.getAnnotation(SuppressChecker.class).value();
	}

	public static boolean canAdapt(Annotation anno, AnnotatedElement element) {
		Collection<ElementType> targets = getTargetElements(anno);
		if (element instanceof Class<?>) {
			return targets.contains(ElementType.TYPE);
		}
		if (element instanceof Method) {
			return targets.contains(ElementType.METHOD);
		}
		if (element instanceof Constructor<?>) {
			return targets.contains(ElementType.CONSTRUCTOR);
		}
		if (element instanceof Field) {
			return targets.contains(ElementType.FIELD);
		}
		return false;
	}

	/**
	 * Get desired target elements. The default implementation reads them from
	 * the {@link CanCheck} annotation on the {@link Checker checker}
	 * annotation.
	 * 
	 * @param anno
	 * @return
	 */
	private static Collection<ElementType> getTargetElements(Annotation anno) {
		return getDescriptor(anno).getCanAdapt();
	}

	private static Collection<ElementType> getTargetElements(
			final Class<? extends Annotation> annotationType) {
		if (!annotationType.isAnnotationPresent(CanCheck.class)) {
			return Arrays.asList(CanCheck.DEFAULTS);
		}
		Collection<ElementType> targets = Arrays.asList(annotationType
				.getAnnotation(CanCheck.class).value());
		return targets;
	}

	private static final Map<Class<? extends Annotation>, CheckerDescriptor> descriptorCache = new HashMap<Class<? extends Annotation>, CheckerDescriptor>();

	@SuppressWarnings("unchecked")
	private static final CheckerDescriptor NULL_DESCRIPTOR = new CheckerDescriptor(
			Arrays.asList(CanCheck.DEFAULTS), 
			Arrays.asList((Class<? extends Checker>[]) new Class[0]),
			InvocationAdaptersPriorities.NONE,
			InvocationAdaptersPriorities.METHOD);

	private static final CheckerDescriptor getDescriptor(Annotation annotation) {
		if (annotation == null) {
			return NULL_DESCRIPTOR;
		}
		final Class<? extends Annotation> anno = annotation.annotationType();
		CheckerDescriptor desc = descriptorCache.get(anno);
		if (desc != null) {
			return desc;
		}
		desc = new CheckerDescriptor(getTargetElements(anno), Arrays
				.asList(suppressCheckers(anno)),
				getMinAdapterPriorityToFail(anno),
				getMinAdapterPriorityToPass(anno));
		descriptorCache.put(anno, desc);
		return desc;
	}

}
