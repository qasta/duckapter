package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.FieldChecker;

/**
 * Denotes that the target element must be a field. Can be only used on methods.
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@CheckerAnnotation(FieldChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
public @interface Field {

}
