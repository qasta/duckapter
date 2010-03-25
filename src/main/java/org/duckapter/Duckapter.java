package org.duckapter;

import java.lang.reflect.Proxy;

import org.duckapter.adapted.AdaptedFactory;
import org.duckapter.annotation.Constructor;
import org.duckapter.annotation.Factory;
import org.duckapter.annotation.Optional;
import org.duckapter.annotation.Static;

/**
 * Duckapter brings support for duck typing into the java programming language.<br />
 * Duckapter has two methods {@link #canAdaptInstance(Object, Class)} and
 * {@link #canAdaptClass(Class, Class)} for finding out whether particular class
 * or instance can be "duck typed" or adapted to the given interface. And two
 * methods {@link #adaptClass(Class, Class)} and
 * {@link #adaptInstance(Object, Class)} which performs the adaptation to the
 * interface.<br />
 * Methods {@link #canAdaptInstance(Object, Class)} and
 * {@link #adaptInstance(Object, Class)} can adapt existing objects. Methods
 * {@link #canAdaptClass(Class, Class)} and
 * {@link #adaptInstance(Object, Class)} adapts class object to access static
 * methods and properties but also constructors.<br/>
 * 
 * @author vladimir.orany
 */
public class Duckapter {

	/**
	 * Tries to adapt existing instance to the interface given as
	 * <code>duck</code>.<br/>
	 * <code>duck</code> interface can be any interface. This means that
	 * <code>duck</code> interface can also has methods annotated with
	 * {@link Static}, {@link Factory} and {@link Constructor}, but its usually
	 * better to keep this methods separately.</br> <code>original</code> must
	 * implement all demanded methods and properties to satisfy
	 * {@link #canAdaptInstance(Object, Class)} method. <br/>
	 * If <code>duck</code> interface has {@link Optional} methods they will
	 * throw {@link UnsupportedOperationException} when called but not
	 * implemented for given instance. <br/>
	 * Both parameters must not be null.
	 * 
	 * @param <T>
	 *            interface to be adapted to and returned
	 * @param original
	 *            instance to be adapted to specified <code>duck</code>
	 *            interface
	 * @param class of duck interface to be adapted to and returned. Must be an
	 *        interface.
	 * @return original instance adapted to given <code>duck</code> interface
	 *         which also implements {@link AdaptedClass} interface
	 * @throws IllegalArgumentException
	 *             if {@link #canAdaptInstance(Object, Class)} returns false for
	 *             given parameters
	 */
	public static <T> T adaptInstance(Object original, Class<T> duck) {
		assertCanDuck(original, duck);
		return duck.cast(Proxy.newProxyInstance(Duckapter.class
				.getClassLoader(), new Class[] { duck, Adapted.class,
				AdaptedClass.class }, AdaptedFactory.adapt(original, original
				.getClass(), duck)));
	}

	/**
	 * Tries to adapt class to the interface given as <code>duck</code>.
	 * <code>duck</code> interface must have only methods annotated with
	 * {@link Static}, {@link Factory} and {@link Constructor} which are
	 * supposed to be access on the class level. <code>original</code> class
	 * must have all this method to satisfy {@link #canAdaptClass(Class, Class)}
	 * method.
	 * 
	 * <br/>
	 * If <code>duck</code> interface has {@link Optional} methods they will
	 * throw {@link UnsupportedOperationException} when called but not
	 * implemented for given class. All return types of
	 * <code>duckInterface</code> must be same as on <code>original</code> or
	 * must be also able to be adapted. <br/>
	 * Both parameters must not be null.
	 * 
	 * @param <T>
	 *            interface to be adapted to and returned
	 * @param original
	 *            original class to be adapted to specified <code>duck</code>
	 *            interface
	 * @param duckInterface
	 *            class of duck interface to be adapted and returned. Must be an
	 *            interface.
	 * @return proxy instance mapping constructors, static methods and
	 *         properties to the given <code>duck</code> interface and which
	 *         also implements {@link AdaptedClass} interface
	 * @throws IllegalArgumentException
	 *             if {@link #canAdaptClass(Class, Class)} returns false for
	 *             given parameters
	 */
	public static <T> T adaptClass(Class<?> original, Class<T> duckInterface) {
		assertClassCanDuck(original, duckInterface);
		return duckInterface.cast(Proxy.newProxyInstance(Duckapter.class
				.getClassLoader(), new Class[] { duckInterface, Adapted.class,
				AdaptedClass.class }, AdaptedFactory.adapt(null, original,
				duckInterface)));
	}

	/**
	 * Determines whether particular class can be adapted to the given
	 * interface. All methods of <code>duckInterface</code> must be annotated
	 * with {@link Static}, {@link Factory} or {@link Constructor} or the
	 * interface itself must be annotated with {@link Static}. <br/>
	 * <code>duckInterface</code> must be an interface. <br/>
	 * All methods not declared as {@link Optional} must be implemented by
	 * <code>original</code> class. <br/>
	 * All return types of <code>duckInterface</code> must be same as on
	 * <code>original</code> or must be also able to be adapted.
	 * 
	 * @param original
	 *            class we want to determine whether is adaptable for given
	 *            <code>duckInterface</code>
	 * @param duckInterface
	 *            interface we want to determine whether <code>original</code>
	 *            class can be adapted to
	 * @return <code>true</code> if <code>duckInterface</code> is an interface
	 *         and all methods not declared as {@link Optional} are implemented
	 *         by <code>original</code> class
	 */
	public static boolean canAdaptClass(Class<?> original,
			Class<?> duckInterface) {
		return AdaptedFactory.adapt(original, duckInterface).canAdaptClass();
	}

	/**
	 * Determines whether particular instance can be adapted to the given
	 * interface. <br/>
	 * <code>duckInterface</code> must be an interface. <br/>
	 * All methods not declared as {@link Optional} must be implemented by
	 * <code>original</code> instance.
	 * 
	 * @param original
	 *            instance we want to determine whether is adaptable for given
	 *            <code>duckInterface</code>
	 * @param duckInterface
	 *            interface we want to determine whether <code>original</code>
	 *            instance can be adapted to
	 * @return <code>true</code> if <code>duckInterface</code> is an interface
	 *         and all methods not declared as {@link Optional} are implemented
	 *         by <code>original</code> instance
	 */
	public static boolean canAdaptInstance(Object original,
			Class<?> duckInterface) {
		return AdaptedFactory.adapt(original.getClass(), duckInterface)
				.canAdaptInstance();
	}

	public static boolean canAdaptInstanceOf(Class<?> classOfOriginal,
			Class<?> duckInterface) {
		System.out.printf("===canAdaptInstanceOf=== original: %s, duck:%s%n",
				classOfOriginal, duckInterface);
		final boolean canAdaptInstance = AdaptedFactory.adapt(classOfOriginal,
				duckInterface).canAdaptInstance();
		System.out.printf(
				"===canAdaptInstanceOf=== !!!%s!!!original: %s, duck:%s%n",
				canAdaptInstance, classOfOriginal, duckInterface);
		return canAdaptInstance;
	}

	private static boolean canAdapt(Class<?> original, Class<?> duck,
			boolean onlyStaticAllowed) {
		AdaptedClass ac = AdaptedFactory.adapt(original, duck);
		if (onlyStaticAllowed) {
			return ac.canAdaptClass();
		} else {
			return ac.canAdaptInstance();
		}
	}

	private static void assertClassCanDuck(Class<?> original, Class<?> duck) {
		if (!canAdapt(original, duck, true)) {
			throw new IllegalArgumentException("Cannot adapt class!"
					+ AdaptedFactory.adapt(original, duck)
							.getUnimplementedForClass());
		}
	}

	private static void assertCanDuck(Object o, Class<?> duck) {
		if (!canAdapt(o.getClass(), duck, false)) {
			throw new IllegalArgumentException("Cannot adapt class!"
					+ AdaptedFactory.adapt(o.getClass(), duck)
							.getUnimplementedForInstance());
		}
	}

}
