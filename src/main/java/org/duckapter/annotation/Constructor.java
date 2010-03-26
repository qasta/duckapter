package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.ConstructorChecker;

/**
 * Declares that annotated method will be constructor for given class.
 * Constructors are matched by method's parameters. For example following method
 * would satisfy all classes with default constructor.
 * 
 * <pre>
 * @code @Constructor Object newInstance();}
 * </pre>
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@CheckerAnnotation(ConstructorChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR})
public @interface Constructor {
}
