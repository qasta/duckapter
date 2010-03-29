package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * Declares that annotated method or all methods of annotated class must be
 * accessed statically. {@link Factory} and {@link Constructor} are supposed to
 * be static automatically.
 * 
 * @author Vladimir Orany
 */
@Documented
@ModifierChecker(Modifier.STATIC)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
public @interface Static {

}
