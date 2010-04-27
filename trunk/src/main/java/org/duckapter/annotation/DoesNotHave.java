package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.DoesNotHaveChecker;

/**
 * If you use this annotation on the duck method you denote that any target
 * element cannot pass the checks. Once used on the duck method the duck method
 * cannot be called and is used only for discrimination.Can only be used on the
 * duck method.
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@CheckerAnnotation(DoesNotHaveChecker.class)
@Target( { ElementType.METHOD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DoesNotHave {

}
