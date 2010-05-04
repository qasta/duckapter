package org.duckapter.adapted;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.duckapter.Adapted;
import org.duckapter.AdaptedClass;

/**
 * The class {@link AdaptedFactory} is factory class for instances of
 * {@link Adapted} and {@link AdaptedClass} interfaces.
 * 
 * @author Vladimir Orany
 * 
 */
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

	/**
	 * Clear cached instances to free the memory or to get fresh results from
	 * {@link #adapt(Class, Class)} and {@link #adapt(Object, Class, Class)}
	 * methods.
	 */
	public static void clearCache() {
		cache.clear();
	}

	/**
	 * Creates an {@link Adapted} for selected parameters. The duck interface
	 * need not to be an interface but in that case returned instance will not
	 * be able of adaptation.
	 * 
	 * @param <O>
	 *            the type of original object
	 * @param <D>
	 *            the duck interface
	 * @param original
	 *            the original object
	 * @param originalClass
	 *            the class of original object
	 * @param duckInterface
	 *            the duck interface
	 * @return {@link Adapted} for selected parameters
	 */
	public static <O, D> Adapted<O, D> adapt(final O original,
			final Class<O> originalClass, final Class<D> duckInterface) {
		if (!duckInterface.isInterface()) {
			return new EmptyAdapted<O, D>(original,
					findAdaptedClass(new Pair<O, D>(originalClass,
							duckInterface)));
		}
		return new AdaptedImpl<O, D>(original, findAdaptedClass(new Pair<O, D>(
				originalClass, duckInterface)));
	}

	/**
	 * Creates an {@link AdaptedClass} for selected parameters or use one from
	 * the pool. The duck interface need not to be an interface but in that case
	 * returned instance will not be able of adaptation.
	 * 
	 * @param <O>
	 *            the type of original object
	 * @param <D>
	 *            the duck interface the original object
	 * @param originalClass
	 *            the class of original object
	 * @param duckInterface
	 *            the duck interface
	 * @return {@link Adapted} for selected parameters
	 */
	public static <O, D> AdaptedClass<O, D> adapt(final Class<O> originalClass,
			final Class<D> duckInterface) {
		return findAdaptedClass(new Pair<O, D>(originalClass, duckInterface));
	}

	private static <O, D> AdaptedClass<O, D> findAdaptedClass(final Pair<O, D> p) {
		AdaptedClass<O, D> ac = getFromCache(p);
		if (ac == null) {
			if (pending.get().contains(p)) {
				ac = new PendingAdaptedClass<O, D>(p.original, p.duck);
			} else {
				pending.get().add(p);
				if (p.duck.isInterface()) {
					ac = new AdaptedClassImpl<O, D>(p.original, p.duck);
				} else {
					ac = new EmptyAdaptedClass<O, D>(p.original, p.duck);
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
		return (AdaptedClass<O, D>) cache.get(p);
	}

}
