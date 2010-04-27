package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.ConstructorChecker;

/**
 * Declares that the target element must be a constructor. Can be only used on
 * the duck method.
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@CheckerAnnotation(ConstructorChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR })
public @interface Constructor {
}
