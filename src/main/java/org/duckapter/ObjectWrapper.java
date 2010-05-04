package org.duckapter;

/**
 * Interface {@link ObjectWrapper} wraps original object and allows it to be adapted
 * to the desired interface.
 * 
 * @author Vladimir Orany
 * 
 * @param <O>
 *            the type of original object
 * @param <D>
 *            the duck interface
 */
public interface ObjectWrapper<O, D> {

	/**
	 * @return the original instance of adapted object
	 */
	O getOriginalInstance();

	/**
	 * @return the adapted class describing the adaptation
	 */
	ClassWrapper<O, D> getAdaptedClass();

	/**
	 * Tries to adapt the original instance to the desired interface. Interface
	 * can contain instance and class level (static) methods.
	 * 
	 * @return adapted instance if possible
	 * @throws WrappingException
	 * @see Duck#isWrappable(Object, Class)
	 * @see Duck#wrap(Object, Class)
	 * @see ClassWrapper#canAdaptInstance()
	 */
	D adaptInstance();

	/**
	 * Tries to adapt the original class to the desired interface. Interface can
	 * contain class level (static) methods but not instance level methods. The
	 * {@link #getOriginalInstance() original instance} is usually
	 * <code>null</code> when this method succeed.
	 * 
	 * @return adapted instance if possible
	 * @throws WrappingException
	 * @see Duck#isWrappable(Class, Class)
	 * @see Duck#wrap(Class, Class)
	 * @see ClassWrapper#canAdaptClass()
	 */
	D adaptClass();

}
