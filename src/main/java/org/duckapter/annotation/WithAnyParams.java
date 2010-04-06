package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.CheckerAnnotation;
import org.duckapter.checker.WithAnyParamsChecker;

@Documented
@CheckerAnnotation(WithAnyParamsChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR})
public @interface WithAnyParams {

}
