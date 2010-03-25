package org.duckapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;


public interface Checker<T extends Annotation> {

	boolean doesCheck(T anno, AnnotatedElement element);

	MethodAdapter check(T anno, AnnotatedElement original, AnnotatedElement duck);

	<A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			AnnotatedElement duckMethod);
}
