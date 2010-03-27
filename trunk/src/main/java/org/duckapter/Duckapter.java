package org.duckapter;

import java.lang.reflect.Proxy;

import org.duckapter.adapted.AdaptedFactory;

public class Duckapter {
	
	private Duckapter() {
		// prevents instance creation and subtyping
	}

	public static <O,D> D adaptInstance(O original, Class<D> duck) {
		return AdaptedFactory.adapt(original, (Class<O>)original.getClass(), duck).adaptInstance();
	}


	public static <O,D> D adaptClass(Class<O> original, Class<D> duck) {
		return AdaptedFactory.adapt(null, original, duck).adaptClass();
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


}
