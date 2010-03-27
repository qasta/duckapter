package org.duckapter.adapted;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

public final class AdaptedFactory {

	@SuppressWarnings("unchecked")
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

	public static <O,D> Adapted<O,D> adapt(O original, Class<O> originalClass,
			Class<D> duckInterface) {
		if (!duckInterface.isInterface()) {
			return new EmptyAdapted<O,D>(original, findAdaptedClass(new Pair<O,D>(
					originalClass, duckInterface)));
		}
		return new AdaptedImpl<O,D>(original, findAdaptedClass(new Pair<O,D>(
				originalClass, duckInterface)));
	}

	public static <O,D> AdaptedClass<O,D> adapt(Class<O> originalClass,
			Class<D> duckInterface) {
		return findAdaptedClass(new Pair<O,D>(originalClass, duckInterface));
	}


	private static <O,D> AdaptedClass<O,D> findAdaptedClass(Pair<O,D> p) {
		AdaptedClass<O,D> ac = getFromCache(p);
		if (ac == null) {
			if (pending.get().contains(p)) {
				ac = new PendingAdaptedClass<O,D>(p.original, p.duck);
			} else {
				pending.get().add(p);
				if (p.duck.isInterface()) {
					ac = new AdaptedClassImpl<O,D>(p.original, p.duck);
				} else {
					ac = new EmptyAdaptedClass<O,D>(p.original, p.duck);
				}
				cache.put(p, ac);
				pending.get().remove(p);
			}
		}
		return ac;
	}

	@SuppressWarnings("unchecked")
	private static Map<Pair, AdaptedClass> cache = new HashMap<Pair, AdaptedClass>();

	@SuppressWarnings("unchecked")
	private static <O, D> AdaptedClass<O, D> getFromCache(Pair<O, D> p) {
		return (AdaptedClass<O, D>)cache.get(p);
	}

}
