package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.DuckAnnotation;
import org.duckapter.annotation.checker.MatchingChecker;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
@DuckAnnotation(MatchingChecker.class)
public @interface Matching {
	String value();

	boolean caseInsensitive() default false;

	boolean dotAllMode() default false;

	boolean unixLines() default false;

	boolean comments() default false;

	boolean multiLine() default false;

	boolean literal() default false;

	boolean unicodeCase() default false;

	boolean canonical() default false;
}
