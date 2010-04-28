package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * Inversion of the {@link Transient} annotation.
 * @author Vladimir Orany
 * @see Negative
 * @see Transient
 */
@Documented
@Negative
@ModifierChecker(Modifier.TRANSIENT)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface NonTransient {

}
