package org.duckapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;


public interface Checker<T extends Annotation> {

	boolean canAdapt(T anno, AnnotatedElement element, Class<?> classOfOriginal);

	InvocationAdapter adapt(T anno, AnnotatedElement original, AnnotatedElement duck, Class<?> classOfOriginal);

	<A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			T anno, AnnotatedElement element);
}
