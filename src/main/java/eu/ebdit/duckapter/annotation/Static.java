package eu.ebdit.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that annotated method or all methods of annotated class must be accessed static.
 * {@link Factory} and {@link Constructor} are supposed to be static automatically.
 * @author Vladimir Orany
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Static {

}
