package org.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.duckapter.Duckapter;

/**
 * Declares that method is property getter/setter. Annotated methods should use
 * JavaBeans naming conventions with get*, set* and is*. Setter methods must
 * return void and have one parameter. Getter methods must return property value
 * and have no parameters. If there is no method of appropriate name
 * {@link Duckapter} will try to find field with name derived from getter or
 * setter method name. If this field is final it will not satisfy annotated
 * setter method. {@link Alias} can contain aslo bare field name without get*,
 * set* or is* prefix.
 * 
 * @author Vladimir Orany
 */
@Documented
@Stereotype(StereotypeType.OR)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Method @Field
public @interface Property {
}
