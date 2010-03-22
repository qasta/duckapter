package org.duckapter.modifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.annotation.Constructor;
import org.duckapter.annotation.Factory;
import org.duckapter.modifier.checker.StaticChecker;

/**
 * Declares that annotated method or all methods of annotated class must be
 * accessed statically. {@link Factory} and {@link Constructor} are supposed to
 * be static automatically.
 * 
 * @author Vladimir Orany
 */
@Documented
@DuckAnnotation(StaticChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface Static {

}
