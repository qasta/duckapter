package org.duckapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface LogicalChecker<T extends Annotation> extends Checker<T> {

	boolean check(T anno, AnnotatedElement original, AnnotatedElement duck, Class<?> classOfOriginal);
	
}
