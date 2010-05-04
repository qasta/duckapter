package org.duckapter.adapted;

import static java.util.Arrays.asList;
import static org.duckapter.adapted.AdaptedClassHelper.getRelevantElements;
import static org.duckapter.adapter.InvocationAdapters.MAX;
import static org.duckapter.adapter.InvocationAdapters.MIN;
import static org.duckapter.adapter.InvocationAdapters.safe;
import static org.duckapter.checker.Checkers.collectCheckers;
import static org.duckapter.checker.Checkers.getMinPriorityToFail;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.AdaptationException;
import org.duckapter.AdaptedClass;
import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.MethodAdapter;
import org.duckapter.checker.Checkers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

final class AdaptedClassImpl<O, D> extends AbstractAdaptedClass<O, D> implements
		AdaptedClass<O, D>, InvocationHandler {

	private final Map<Method, InvocationAdapter> adapters;
	private D proxy;
	private Constructor<?> proxyClassConstructor;
	private AdaptedClass<O, D> detailedClass = null;
	private final boolean detailed;

	AdaptedClassImpl(Class<O> originalClass, Class<D> duckInterface) {
		this(originalClass, duckInterface, false);
	}

	AdaptedClassImpl(Class<O> originalClass, Class<D> duckInterface,
			boolean detailed) {
		super(duckInterface, originalClass);
		this.detailed = detailed;
		Map<Method, InvocationAdapter> builder = Maps
				.newHashMapWithExpectedSize(duckInterface.getMethods().length);
		init(builder);
		adapters = ImmutableMap.copyOf(builder);
	}

	private AdaptedClass<O, D> getDetailedClass() {
		if (detailedClass == null) {
			detailedClass = new AdaptedClassImpl<O, D>(getOriginalClass(),
					getDuckInterface(), true);
			AdaptedFactory.updateCacheInstance(detailedClass);
		}
		return detailedClass;
	}

	public Collection<Method> getUnimplementedForClass() {
		return getDetailedClass().getUnimplementedForClass();
	}

	public Collection<Method> getUnimplementedForInstance() {
		return getDetailedClass().getUnimplementedForClass();
	}

	public Object invoke(Object originalInstance, Method duckMethod,
			Object[] args) throws Throwable {
		return adapters.get(duckMethod).invoke(originalInstance, args);
	}

	private void init(Map<Method, InvocationAdapter> builder) {
		checkClass(builder);
		checkDuckMethods(builder);
		addObjectMethods(builder);
	}

	private void checkClass(Map<Method, InvocationAdapter> builder) {
		updateCanAdapt(resolveAdapter(getOriginalClass(), getDuckInterface(),
				collectCheckers(getDuckInterface()), builder));
	}

	private InvocationAdapter checkDuckMethod(Method duckMethod,
			Map<Method, InvocationAdapter> builder) {
		Map<Checker<Annotation>, Annotation> methodCheckers = collectCheckers(duckMethod);
		InvocationAdapter ret = initialForDuckMethod(duckMethod, builder);
		int minToPass = Checkers.getMinPriorityToPass(methodCheckers);
		for (AnnotatedElement element : getRelevantElements(getOriginalClass())) {
			final InvocationAdapter adapter = resolveAdapter(element,
					duckMethod, methodCheckers, builder);
			ret = mergeAdaptersFromElements(ret, adapter);
			if (ret.getPriority() >= minToPass) {
				return ret;
			}
		}
		return ret;
	}

	private InvocationAdapter initialForDuckMethod(Method duckMethod,
			Map<Method, InvocationAdapter> builder) {
		return safe(builder.get(duckMethod), MIN);
	}

	private void addObjectMethods(Map<Method, InvocationAdapter> builder) {
		if (cannotBeAdaptedAnyway()) {
			return;
		}
		for (Method m : Object.class.getMethods()) {
			builder.put(m, new MethodAdapter(m, m));
		}
	}

	private Map<Checker<Annotation>, Annotation> copy(
			Map<Checker<Annotation>, Annotation> checkersMap) {
		return new LinkedHashMap<Checker<Annotation>, Annotation>(checkersMap);
	}

	private final InvocationAdapter resolveAdapter(AnnotatedElement original,
			AnnotatedElement duck,
			Map<Checker<Annotation>, Annotation> checkersMap,
			Map<Method, InvocationAdapter> builder) {
		Map<Checker<Annotation>, Annotation> checkers = copy(checkersMap);
		int minPriority = getMinPriorityToFail(checkers);
		InvocationAdapter ret = initialForElement(duck, builder);
		for (Entry<Checker<Annotation>, Annotation> entry : checkers.entrySet()) {
			final Checker<Annotation> ch = entry.getKey();
			final Annotation anno = entry.getValue();
			if (ch.canAdapt(anno, original, getOriginalClass())) {
				final InvocationAdapter adapter = ch.adapt(anno, original,
						duck, getOriginalClass());
				ret = mergeAdaptersFromCheckers(ret, adapter);
				if (ret.getPriority() <= minPriority) {
					return ret;
				}
			}
		}
		return ret;
	}

	private InvocationAdapter initialForElement(AnnotatedElement duck,
			Map<Method, InvocationAdapter> builder) {
		return safe(builder.get(duck), MAX);
	}

	private Iterable<Method> getDuckMethods() {
		return asList(getDuckInterface().getMethods());
	}

	private void checkDuckMethods(Map<Method, InvocationAdapter> builder) {
		if (cannotBeAdaptedAnyway()) {
			return;
		}
		for (Method duckMethod : getDuckMethods()) {
			InvocationAdapter adapter = checkDuckMethod(duckMethod, builder);
			updateCanAdapt(adapter);
			builder.put(duckMethod, adapter);
			if (cannotBeAdaptedAnyway()) {
				return;
			}
		}
	}

	private boolean cannotBeAdaptedAnyway() {
		return detailed && (!canAdaptClass && !canAdaptInstance);
	}

	private void updateCanAdapt(InvocationAdapter adapter) {
		canAdaptClass = canAdaptClass && adapter.isInvocableOnClass();
		canAdaptInstance = canAdaptInstance && adapter.isInvocableOnInstance();
	}

	private InvocationAdapter mergeAdaptersFromElements(InvocationAdapter ret,
			final InvocationAdapter adapter) {
		if (ret.getPriority() >= adapter.getPriority()) {
			return ret.orMerge(adapter);
		}
		return adapter.orMerge(ret);
	}

	private InvocationAdapter mergeAdaptersFromCheckers(InvocationAdapter ret,
			final InvocationAdapter adapter) {
		if (ret.getPriority() >= adapter.getPriority()) {
			return ret.andMerge(adapter);
		}
		return adapter.andMerge(ret);
	}

	public D adaptInstance(O instance) {
		if (!canAdaptInstance()) {
			throw new AdaptationException(this);
		}
		return createProxy(instance);
	}

	private D getProxy() {
		if (proxy == null) {
			proxy = createProxy();
		}
		return proxy;
	}

	public D adaptClass() {
		if (!canAdaptClass()) {
			throw new AdaptationException(this);
		}
		return getProxy();
	}

	private D createProxy() {
		return createProxyFor(this);
	}

	private D createProxy(O instance) {
		return createProxyFor(new AdaptedImpl<O, D>(instance, this));
	}

	@SuppressWarnings("unchecked")
	private D createProxyFor(InvocationHandler handler){
		if (proxyClassConstructor == null) {
			initProxyConstructor();
		}
		try {
			return (D) proxyClassConstructor.newInstance(handler);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new IllegalStateException("Cannot create proxy!");
	}
	
	private void initProxyConstructor() {
		Class<?> proxyClass = Proxy.getProxyClass(AdaptedClassImpl.class.getClassLoader(),
				getDuckInterface());
		try {
			proxyClassConstructor = proxyClass.getConstructor(InvocationHandler.class);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
