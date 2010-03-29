package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

@Documented
@ModifierChecker(Modifier.TRANSIENT)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface Transient {

}
