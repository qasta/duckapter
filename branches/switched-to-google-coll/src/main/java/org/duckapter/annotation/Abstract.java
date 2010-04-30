package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * The substitute for the <code>abstract</code> keyword. Can be used on the duck
 * interface or the duck method.
 * 
 * @author Vladimir Orany
 * 
 */
@Documented
@ModifierChecker(Modifier.ABSTRACT)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface Abstract {

}
