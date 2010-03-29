package org.duckapter.annotation;

import static java.lang.reflect.Modifier.FINAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@ModifierChecker(FINAL)
@Target( { ElementType.METHOD, ElementType.TYPE, ElementType.FIELD,
		ElementType.CONSTRUCTOR, ElementType.PARAMETER,
		ElementType.ANNOTATION_TYPE })
public @interface Final {

}
