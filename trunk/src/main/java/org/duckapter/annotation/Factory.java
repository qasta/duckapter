package org.duckapter.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.duckapter.annotation.StereotypeType.OR;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.duckapter.Duck;

/**
 * Declares that annotated method will be factory class or constructor for given
 * class. Factories are matched as any other methods but if no method matches,
 * {@link Duck} will try to match constructor. <br/>
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
@StereotypeChecker(OR)
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Constructor
@StaticField
@StaticMethod
public @interface Factory {
	
}
