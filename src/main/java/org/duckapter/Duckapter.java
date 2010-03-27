package org.duckapter;

import java.lang.reflect.Proxy;

import org.duckapter.adapted.AdaptedFactory;

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
	
	private Duckapter() {
		// prevents instance creation and subtyping
	}

	public static <O,D> D adaptInstance(O original, Class<D> duck) {
		assertCanDuck(original, duck);
		@SuppressWarnings("unchecked")
		Class<O> originalClass = (Class<O>) original.getClass();
		return duck.cast(Proxy.newProxyInstance(Duckapter.class
				.getClassLoader(), new Class[] { duck, Adapted.class,
				AdaptedClass.class }, AdaptedFactory.adapt(original, originalClass, duck)));
	}


	public static <T> T adaptClass(Class<?> original, Class<T> duckInterface) {
		assertClassCanDuck(original, duckInterface);
		return duckInterface.cast(Proxy.newProxyInstance(Duckapter.class
				.getClassLoader(), new Class[] { duckInterface, Adapted.class,
				AdaptedClass.class }, AdaptedFactory.adapt(null, original,
				duckInterface)));
	}


	public static boolean canAdaptClass(Class<?> original,
			Class<?> duckInterface) {
		return AdaptedFactory.adapt(original, duckInterface).canAdaptClass();
	}

	public static boolean canAdaptInstance(Object original,
			Class<?> duckInterface) {
		return AdaptedFactory.adapt(original.getClass(), duckInterface)
				.canAdaptInstance();
	}

	public static boolean canAdaptInstanceOf(Class<?> classOfOriginal,
			Class<?> duckInterface) {
		return AdaptedFactory.adapt(classOfOriginal,
				duckInterface).canAdaptInstance();
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
