package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.NonCheckerChecker;

@Documented
@CheckerAnnotation(NonCheckerChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Negative {
	int checkerPriority() default Integer.MAX_VALUE - 5000;
}
