package org.duckapter.wrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.duckapter.ObjectWrapper;
import org.duckapter.ClassWrapper;

/**
 * The class {@link WrapperFactory} is factory class for instances of
 * {@link ObjectWrapper} and {@link ClassWrapper} interfaces.
 * 
 * @author Vladimir Orany
 * 
 */
public final class WrapperFactory {

	@SuppressWarnings("unchecked")
	private static ThreadLocal<Set<Pair>> pending = new ThreadLocal<Set<Pair>>() {
		@Override
		protected Set<Pair> initialValue() {
			return new HashSet<Pair>();
		}
	};

	private WrapperFactory() {
		// prevents instance creation and subtyping
	}

	/**
	 * Clear cached instances to free the memory or to get fresh results from
	 * {@link #adapt(Class, Class)} and {@link #adapt(Object, Class, Class)}
	 * methods.
	 */
	public static void clearCache() {
		wrapperPool.clear();
	}

	/**
	 * Creates an {@link ObjectWrapper} for selected parameters. The duck interface
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
	 * @return {@link ObjectWrapper} for selected parameters
	 */
	public static <O, D> ObjectWrapper<O, D> adapt(final O original,
			final Class<O> originalClass, final Class<D> duckInterface) {
		if (!duckInterface.isInterface()) {
			return new EmptyObjectWrapper<O, D>(original,
					findAdaptedClass(new Pair<O, D>(originalClass,
							duckInterface)));
		}
		return new ObjectWrapperImpl<O, D>(original, findAdaptedClass(new Pair<O, D>(
				originalClass, duckInterface)));
	}

	/**
	 * Creates an {@link ClassWrapper} for selected parameters or use one from
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
	 * @return {@link ObjectWrapper} for selected parameters
	 */
	public static <O, D> ClassWrapper<O, D> adapt(final Class<O> originalClass,
			final Class<D> duckInterface) {
		return findAdaptedClass(new Pair<O, D>(originalClass, duckInterface));
	}

	private static <O, D> ClassWrapper<O, D> findAdaptedClass(final Pair<O, D> p) {
		ClassWrapper<O, D> ac = getFromCache(p);
		if (ac == null) {
			if (pending.get().contains(p)) {
				ac = new PendingClassWrapper<O, D>(p.original, p.duck);
			} else {
				pending.get().add(p);
				if (p.duck.isInterface()) {
					ac = new ClassWrapperImpl<O, D>(p.original, p.duck);
				} else {
					ac = new EmptyClassWrapper<O, D>(p.original, p.duck);
				}
				wrapperPool.put(p, ac);
				pending.get().remove(p);
			}
		}
		return ac;
	}

	@SuppressWarnings("unchecked")
	private static Map<Pair, ClassWrapper> wrapperPool = new HashMap<Pair, ClassWrapper>();

	@SuppressWarnings("unchecked")
	private static <O, D> ClassWrapper<O, D> getFromCache(Pair<O, D> p) {
		return (ClassWrapper<O, D>) wrapperPool.get(p);
	}

}
