package org.duckapter.modifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.modifier.checker.VolatileChecker;

@Documented
@DuckAnnotation(VolatileChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface Volatile {

}
