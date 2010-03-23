package org.duckapter.adapted;

import java.util.HashMap;
import java.util.Map;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

public final class AdaptedFactory {

	private AdaptedFactory() {
		// prevents instance creation and subtyping
	}

	public static void clearCache(){
		cache.clear();
	}
	
	public static Adapted adapt(Object original, Class<?> originalClass,
			Class<?> duckInterface) {
		if (!duckInterface.isInterface()) {
			return new EmptyAdapted(original, findAdaptedClass(new Pair(
					originalClass, duckInterface)));
		}
		return new AdaptedImpl(original, findAdaptedClass(new Pair(
				originalClass, duckInterface)));
	}

	public static AdaptedClass adapt(Class<?> originalClass,
			Class<?> duckInterface) {
		return findAdaptedClass(new Pair(originalClass, duckInterface));
	}

	private static Map<Pair, AdaptedClass> cache = new HashMap<Pair, AdaptedClass>();

	private static AdaptedClass findAdaptedClass(Pair p) {
		AdaptedClass ac = cache.get(p);
		if (ac == null) {
			if (p.duck.isInterface()) {
				ac = new AdaptedClassImpl(p.original, p.duck);
			} else {
				ac = new EmptyAdaptedClass(p.original, p.duck);
			}
			
			cache.put(p, ac);
		}
		return ac;
	}

	private static final class Pair {
		private final Class<?> original;
		private final Class<?> duck;

		public Pair(Class<?> original, Class<?> duck) {
			this.original = original;
			this.duck = duck;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((duck == null) ? 0 : duck.hashCode());
			result = prime * result
					+ ((original == null) ? 0 : original.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (duck == null) {
				if (other.duck != null)
					return false;
			} else if (!duck.equals(other.duck))
				return false;
			if (original == null) {
				if (other.original != null)
					return false;
			} else if (!original.equals(other.original))
				return false;
			return true;
		}

	}

}
