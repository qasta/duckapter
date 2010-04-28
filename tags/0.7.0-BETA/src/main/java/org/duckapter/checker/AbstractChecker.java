package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.duckapter.Checker;

/**
 * The base class for implementation of the {@link Checker} interface. It
 * provides simplified {@link #canAdapt(Annotation, AnnotatedElement, Class)}
 * method which is based on reading checker annotation {@link Target}
 * annotation. The targets are cached. The {@link #hashCode()} method is
 * optimalized and the equals method delegates the
 * {@link Checkers#equals(Object)} method.
 * 
 * @author Vladimir Orany
 * 
 * @param <T>
 *            the annotation bind to the checker
 */
public abstract class AbstractChecker<T extends Annotation> implements
		Checker<T> {

	/**
	 * The list of all possible targets. Returning this value from the
	 * {@link #getTargetElements(Annotation)} method will result in allowing any
	 * element to be adapted.
	 */
	protected static final List<ElementType> ALL_TARGETS = Arrays
			.asList(ElementType.values());
	private final int hashCode;

	/**
	 * Creates new instance of this class.
	 */
	public AbstractChecker() {
		this.hashCode = Checkers.hashCode(getClass());
	}

	@SuppressWarnings("unchecked")
	public final boolean canAdapt(T anno, AnnotatedElement element,
			Class<?> classOfOriginal) {
		Collection<ElementType> targets = getTargetElements(anno);
		if (element instanceof Class) {
			return targets.contains(ElementType.TYPE);
		}
		if (element instanceof Method) {
			return targets.contains(ElementType.METHOD);
		}
		if (element instanceof Constructor) {
			return targets.contains(ElementType.CONSTRUCTOR);
		}
		if (element instanceof Field) {
			return targets.contains(ElementType.FIELD);
		}
		return false;
	}

	private static final Map<Class<? extends Annotation>, Collection<ElementType>> cachedTargets = new HashMap<Class<? extends Annotation>, Collection<ElementType>>();

	/**
	 * Get desired target elements. The default implementation reads them from
	 * the {@link Target} annotation on the {@link Checker checker} annotation.
	 * @param anno
	 * @return
	 */
	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno == null) {
			throw new IllegalArgumentException(
					"Cannot obtrain target elements! " + getClass());
		}
		final Class<? extends Annotation> annotationType = anno
				.annotationType();
		Collection<ElementType> fromCache = cachedTargets.get(annotationType);
		if (fromCache != null) {
			return fromCache;
		}
		if (!annotationType.isAnnotationPresent(Target.class)) {
			cachedTargets.put(annotationType, ALL_TARGETS);
			return ALL_TARGETS;
		}
		Collection<ElementType> targets = Arrays.asList(annotationType
				.getAnnotation(Target.class).value());
		cachedTargets.put(annotationType, targets);
		return targets;
	}

	public <A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			T anno, AnnotatedElement element) {
		return Collections.emptyList();
	}

	@Override
	public final boolean equals(Object obj) {
		return Checkers.equals(this, obj);
	}

	@Override
	public final int hashCode() {
		return hashCode;
	}

}