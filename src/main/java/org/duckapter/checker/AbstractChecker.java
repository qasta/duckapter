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
import java.util.List;

import org.duckapter.Checker;

public abstract class AbstractChecker<T extends Annotation> implements
		Checker<T> {

	protected static final List<ElementType> ALL_TARGETS = Arrays.asList(ElementType.values());
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

	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno == null) {
			throw new IllegalArgumentException(
					"Cannot obtrain target elements! " + getClass());
		}
		if (!anno.annotationType().isAnnotationPresent(Target.class)) {
			return ALL_TARGETS;
		}
		Collection<ElementType> targets = Arrays.asList(anno.annotationType()
				.getAnnotation(Target.class).value());
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