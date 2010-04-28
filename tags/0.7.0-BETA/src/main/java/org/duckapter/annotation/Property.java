package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.checker.StereotypeType;

/**
 * Declares that the target element must be method or field. Can be only used on
 * the duck method.
 * 
 * @author Vladimir Orany
 */
@Documented
@StereotypeChecker(StereotypeType.OR)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.FIELD })
@Method
@Field
public @interface Property {
}
