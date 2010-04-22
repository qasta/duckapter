package org.duckapter;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Vladimir Orany
 * 
 * @param <O> the original class
 * @param <D> the desired interface
 */
public interface AdaptedClass<O, D> {

	/**
	 * @param object
	 * @param duckMethod
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	Object invoke(O object, Method duckMethod, Object[] args) throws Throwable;

	/**
	 * @return the original class
	 */
	Class<O> getOriginalClass();

	/**
	 * @return the desired duck interface
	 */
	Class<D> getDuckInterface();

	/**
	 * @return whether the instances of original class can be adapted to the
	 *         desired interface
	 */
	boolean canAdaptInstance();

	/**
	 * 
	 * @return whether the original class can be adapted to the desired duck
	 *         interface
	 */
	boolean canAdaptClass();

	/**
	 * @return {@link Collection} of unimplemented methods at instance level for
	 *         specified pair original class - duck interface
	 */
	Collection<Method> getUnimplementedForInstance();

	/**
	 * @return {@link Collection} of unimplemented methods at class level for
	 *         specified pair original class - duck interface
	 */
	Collection<Method> getUnimplementedForClass();

}
