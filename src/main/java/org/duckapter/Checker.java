package org.duckapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

/**
 * Checkers checks whether particular annotated element (class, constructor,
 * method or field) could be adapted to some other annotated duck element (duck
 * interface or method). If necessary one can suppress some other checkers.
 * Checker is usually bind to some annotation which signalizes its usage on the
 * class or method.
 * 
 * @author Vladimir Orany
 * 
 * @param <T>
 *            annotation binded to the checker
 * @see InvocationAdapter
 */
public interface Checker<T extends Annotation> {

	/**
	 * Returns <code>true</code> if this checker can adapt the type of
	 * particular element, returns <code>false</code> if there must be used some
	 * other checker to decide that.
	 * 
	 * @param anno
	 *            the annotation from the duck element (method or class)
	 * @param element
	 *            the checked element
	 * @param classOfOriginal
	 *            the original class of checked element
	 * @return whether this checker can adapt particular element
	 */
	boolean canAdapt(T anno, AnnotatedElement element, 
			Class<?> classOfOriginal);

	/**
	 * Return instance of {@link InvocationAdapter} interface which either
	 * denote that checked element pass the checker test or adapts checked
	 * elements to the duck element. For the first case use one of
	 * {@link org.duckapter.adapter.InvocationAdapters} predefined instances.
	 * 
	 * @param anno
	 *            the annotation from the duck element
	 * @param original
	 *            the checked element
	 * @param duck
	 *            the duck element
	 * @param classOfOriginal
	 *            the original class of checked element
	 * @return instance of {@link InvocationAdapter} interface which either
	 *         denote that checked element pass the checker test or adapts
	 *         checked elements to the duck element.
	 */
	InvocationAdapter adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck, Class<?> classOfOriginal);

	/**
	 * Returns collection of checkers to be suppressed.
	 * 
	 * @param <A>
	 *            the type of annotation used by the checker
	 * @param <Ch>
	 *            the type of the checker
	 * @param anno
	 *            the annotation from the duck element (method or class)
	 * @param element
	 *            the checked element
	 * @return collection of checkers to be suppressed
	 */
	<A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> 
		suppressCheckers(T anno, AnnotatedElement element);
	
	/**
	 * @param anno TODO
	 * @return the minimal adapter's priority to fail
	 */
	int getMinAdapterPriorityToFail(Annotation anno);

	/**
	 * @param anno TODO
	 * @return
	 */
	int getMinAdapterPriorityToPass(Annotation anno);
}
