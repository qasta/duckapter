package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.checker.SynchronizedChecker;

@Documented
@DuckAnnotation(SynchronizedChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface Synchronized {

}