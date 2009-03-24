package eu.ebdit.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that annotated method or all method of annotated interface needn't to be implemented.
 * If original class or instance does not implement given method {@link UnsupportedOperationException}
 * must be thrown.
 * @author Vladimir Orany
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Optional {

}
