package org.duckapter;

import org.duckapter.wrapper.WrapperFactory;

/**
 * The {@link Duck} class is facade for the Duckapter framework. It provides the
 * core methods {@link #wrap(Class, Class)}, {@link #wrap(Object, Class)},
 * {@link #isWrappable(Class, Class)} and {@link #wrap(Object, Class)} which can users
 * use for duck testing and duck typing. 
 * 
 * @author Vladimir Orany
 */
public final class Duck {

	/**
	 * Private constructor prevents creation of new instances of this class.
	 */
	private Duck() {
		// prevents instance creation and subtyping
	}

	/**
	 * Adapts original object to the duck interface. The method
	 * {@link #isWrappable(Object, Class)} must return <code>true</code> for the same
	 * pair of parameters otherwise {@link WrappingException} will be thrown.
	 * ObjectWrapper object can access instance and class (static) level elements of
	 * original instance.
	 * 
	 * @see #isWrappable(Object, Class)
	 * 
	 * @param <O>
	 *            the type of original object
	 * @param <D>
	 *            the duck interface
	 * @param original
	 *            the object to be adapted
	 * @param duck
	 *            the desired interface of adapted object
	 * @return original object adapted to the duck interface if possible
	 * @throws WrappingException
	 */
	public static <O, D> D wrap(final O original, final Class<D> duck) {
		@SuppressWarnings("unchecked")
		final Class<O> originalClass = (Class<O>) original.getClass();
		return WrapperFactory.adapt(original, originalClass, duck)
				.adaptInstance();
	}

	/**
	 * Adapts original class object to the duck interface. The method
	 * {@link #isWrappable(Class, Class)} must return <code>true</code> for the same
	 * pair of parameters otherwise {@link WrappingException} will be thrown.
	 * ObjectWrapper object can access only class level (static) elements.
	 * Constructors are considered static elements too.
	 * 
	 * @see #isWrappable(Class, Class)
	 * 
	 * @param <O>
	 *            the original class
	 * @param <D>
	 *            the duck interface
	 * @param original
	 *            the class to be adapted
	 * @param duck
	 *            the desired interface of adapted object
	 * @return original class object adapted to the duck interface
	 * @throws WrappingException
	 */
	public static <O, D> D wrap(final Class<O> original, final Class<D> duck) {
		return WrapperFactory.adapt(null, original, duck).adaptClass();
	}

	/**
	 * Tests whether the original class object can be adapted to the specified
	 * duck interface. The following conditions must be met:
	 * <ul>
	 * <li>parameter <code>duck</code> must be an interface
	 * <li>all methods declared by the <code>duck</code> interface must be
	 * accessible on the class level. This practically mean that must be
	 * annotated by {@link Static}, {@link Constructor} or other "static"
	 * annotation like {@link Factory}.
	 * <li>all active {@link Checkers#getDefaultCheckers() default checkers}
	 * must comply for class and each duck interface method where appropriate
	 * <li>all checkers from duck interface must comply for the original class
	 * <li>all methods from the duck interface must have their counterpart
	 * unless the {@link Checker checker} from annotation declares not to need
	 * them
	 * </ul>
	 * 
	 * @param <O>
	 *            the original class
	 * @param <D>
	 *            the duck interface
	 * @param original
	 *            the class to be adapted
	 * @param duck
	 *            the desired interface of adapted object
	 * @return whether the original class can be adapted to the desired
	 *         interface
	 */
	public static <O, D> boolean isWrappable(final Class<O> original,
			final Class<D> duck) {
		return WrapperFactory.adapt(original, duck).canAdaptClass();
	}

	/**
	 * Tests whether the original class object can be adapted to the specified
	 * duck interface. The following conditions must be met:
	 * <ul>
	 * <li>parameter <code>duck</code> must be an interface
	 * <li>all active {@link Checkers#getDefaultCheckers() default checkers}
	 * must comply for class and each duck interface method where appropriate
	 * <li>all checkers from duck interface must comply for the original class
	 * <li>all methods from the duck interface must have their counterpart
	 * unless the {@link Checker checker} from annotation declares not to need
	 * them
	 * </ul>
	 * 
	 * @param <O>
	 *            the type of original object
	 * @param <D>
	 *            the duck interface
	 * @param original
	 *            the object to be adapted
	 * @param duck
	 *            the desired interface of adapted object
	 * @return whether the original object can be adapted to the desired
	 *         interface
	 */
	public static <O, D> boolean isWrappable(final O original, final Class<D> duck) {
		return WrapperFactory.adapt(original.getClass(), duck)
				.canAdaptInstance();
	}

}
