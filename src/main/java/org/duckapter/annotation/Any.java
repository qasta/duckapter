package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the target element can have any name. Can be only used on the
 * duck method.
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.FIELD })
@StereotypeChecker
@Matching(".*")
public @interface Any {

}
