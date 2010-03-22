package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.Duckapter;
import org.duckapter.annotation.checker.AliasChecker;

/**
 * Allows annotated method or property to has more potential names. <br/>
 * For example if you try to adapt folowing method
 * 
 * <pre>
 * @code @Alias("otherName") void originalName(); }
 * </pre> {@link Duckapter} will first look whether there is method called
 * <code>originalName</code> and if there isn't it will try to find method
 * called <code>otherName</code>. Both this methods will satisfy demanded method
 * of interface.
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@DuckAnnotation(AliasChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Alias {

	String[] value();

}
