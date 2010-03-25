package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.Duckapter;
import org.duckapter.checker.FactoryChecker;

/**
 * Declares that annotated method will be factory class or constructor for given
 * class. Factories are matched as any other methods but if no method matches,
 * {@link Duckapter} will try to match constructor. <br/>
 * For example following method will match with all classes which declares
 * method <code>valueOf(String)</code> or have constructor which takes single
 * {@link String} parameter.
 * 
 * <pre>
 * @code @Factory Object valueOf(String s);}
 * </pre>
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@DuckAnnotation(FactoryChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Factory {

}
