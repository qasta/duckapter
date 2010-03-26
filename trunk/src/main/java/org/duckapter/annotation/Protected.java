package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.VisibilityChecker;

@Documented
@CheckerAnnotation(VisibilityChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE , ElementType.ANNOTATION_TYPE})
public @interface Protected {
	Visibility value() default Visibility.EXACT;
}
