package org.duckapter.adapted;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

public final class AdaptedFactory {

	private static ThreadLocal<Set<Pair>> pending = new ThreadLocal<Set<Pair>>() {
		@Override
		protected Set<Pair> initialValue() {
			return new HashSet<Pair>();
		}
	};

	private AdaptedFactory() {
		// prevents instance creation and subtyping
	}

	public static void clearCache() {
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
			if (pending.get().contains(p)) {
				ac = new PendingAdaptedClass(p.original, p.duck);
			} else {
				pending.get().add(p);
				if (p.duck.isInterface()) {
					ac = new AdaptedClassImpl(p.original, p.duck);
				} else {
					ac = new EmptyAdaptedClass(p.original, p.duck);
				}
				cache.put(p, ac);
				pending.get().remove(p);
			}
		}
		return ac;
	}

}
