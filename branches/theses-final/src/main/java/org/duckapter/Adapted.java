package org.duckapter;

/**
 * Interface {@link Adapted} wraps original object and allows it to be adapted
 * to the desired interface.
 * 
 * @author Vladimir Orany
 * 
 * @param <O>
 *            the type of original object
 * @param <D>
 *            the duck interface
 */
public interface Adapted<O, D> {

	/**
	 * @return the original instance of adapted object
	 */
	O getOriginalInstance();

	/**
	 * @return the adapted class describing the adaptation
	 */
	AdaptedClass<O, D> getAdaptedClass();

	/**
	 * Tries to adapt the original instance to the desired interface. Interface
	 * can contain instance and class level (static) methods.
	 * 
	 * @return adapted instance if possible
	 * @throws AdaptationException
	 * @see Duck#test(Object, Class)
	 * @see Duck#type(Object, Class)
	 * @see AdaptedClass#canAdaptInstance()
	 */
	D adaptInstance();

	/**
	 * Tries to adapt the original class to the desired interface. Interface can
	 * contain class level (static) methods but not instance level methods. The
	 * {@link #getOriginalInstance() original instance} is usually
	 * <code>null</code> when this method succeed.
	 * 
	 * @return adapted instance if possible
	 * @throws AdaptationException
	 * @see Duck#test(Class, Class)
	 * @see Duck#type(Class, Class)
	 * @see AdaptedClass#canAdaptClass()
	 */
	D adaptClass();

}
