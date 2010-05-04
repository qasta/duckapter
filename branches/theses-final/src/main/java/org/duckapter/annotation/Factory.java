package org.duckapter.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.duckapter.checker.StereotypeType.OR;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares that the target element must be a constructor, a static method or a
 * static field. Can be only used on the duck method.
 * @author Vladimir Orany
 * 
 */
@Documented
@StereotypeChecker(OR)
@Retention(RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.FIELD })
@Constructor
@StaticField
@StaticMethod
public @interface Factory {
}
