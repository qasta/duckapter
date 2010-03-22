package org.duckapter.modifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.modifier.checker.PrivateChecker;

@Documented
@DuckAnnotation(PrivateChecker.class)
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Private {
	Visibility value() default Visibility.EXACT;
}
