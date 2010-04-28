package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.AllChecker;

/**
 * The duck method annotated with this annotation gathers all targets elements
 * which succeed the check. All checks from annotations are performed against
 * the duck method besides the default checks for the return type, for the
 * method parameters and for the exceptions. Those are checked against the
 * single method of the interface which is component of the array which is
 * declared as the return type of the duck method. In there are no target
 * elements which succeed an empty array is returned when the annotated method
 * is invoked. Can be used only on the duck methods.
 * 
 * @author Vladimir Orany
 */
@Documented
@CheckerAnnotation(AllChecker.class)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface All {

}
