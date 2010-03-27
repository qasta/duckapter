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

public abstract class AbstractChecker<T extends Annotation> implements
		Checker<T> {

	protected static final List<ElementType> ALL_TARGETS = Arrays
			.asList(ElementType.values());
	private final int hashCode;

	public AbstractChecker() {
		this.hashCode = Checkers.hashCode(getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public final boolean canAdapt(T anno, AnnotatedElement element) {
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

	private static final Map<Class<? extends Annotation>, Collection<ElementType>> cachedTargets 
		= new HashMap<Class<? extends Annotation>, Collection<ElementType>>();

	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno == null) {
			throw new IllegalArgumentException(
					"Cannot obtrain target elements! " + getClass());
		}
		final Class<? extends Annotation> annotationType = anno.annotationType();
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

	@Override
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