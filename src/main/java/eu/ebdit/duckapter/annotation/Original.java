package eu.ebdit.duckapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that annotated parameter must be compatibile with original class.
 * <br/>
 * <b>WARNING:</b><br/>
 * Use this annotation judiciously. Method using this annotation can be easily type unsafe
 * because allows wrong parameter to be passed.
 * <br/>
 * Typical example use case is copy method:
 * <pre>{@code @Static ICopying copy(@Original ICopying original);}
 * </pre>
 * <br/> You must always ensure that you will not pass instance of type with is not compatibile
 * with adapted instance.
 * @author Vladimir Orany
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Original {}
