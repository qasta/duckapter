package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.AssignableToChecker;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@CheckerAnnotation(AssignableToChecker.class)
@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface AssignableTo {
	Class<?>[] value();
}
