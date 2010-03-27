package org.duckapter;

import org.duckapter.adapted.AdaptedFactory;

public class Duck {

	private Duck() {
		// prevents instance creation and subtyping
	}
	
	
	public static <O, D> D type(O original, Class<D> duck) {
		@SuppressWarnings("unchecked")
		final Class<O> originalClass = (Class<O>) original.getClass();
		return AdaptedFactory.adapt(original, originalClass, duck)
				.adaptInstance();
	}

	public static <O, D> D type(Class<O> original, Class<D> duck) {
		return AdaptedFactory.adapt(null, original, duck).adaptClass();
	}

	public static <O, D> boolean test(Class<O> original,
			Class<D> duckInterface) {
		return AdaptedFactory.adapt(original, duckInterface).canAdaptClass();
	}

	public static <O, D> boolean test(O original,
			Class<D> duckInterface) {
		return AdaptedFactory.adapt(original.getClass(), duckInterface)
				.canAdaptInstance();
	}

}
