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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.AdaptationException;
import org.duckapter.AdaptedClass;
import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.MethodAdapter;
import org.duckapter.checker.Checkers;

final class AdaptedClassImpl<O, D> extends AbstractAdaptedClass<O, D> implements
		AdaptedClass<O, D>, InvocationHandler {

	private final Map<Method, InvocationAdapter> adapters = new HashMap<Method, InvocationAdapter>();
	private D proxy;
	private AdaptedClass<O, D> detailedClass = null;
	private final boolean detailed;

	AdaptedClassImpl(Class<O> originalClass, Class<D> duckInterface) {
		this(originalClass, duckInterface, false);
	}
	
	
	AdaptedClassImpl(Class<O> originalClass, Class<D> duckInterface, boolean detailed) {
		super(duckInterface, originalClass);
		this.detailed = detailed;
		init();
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

	private void init() {
		checkClass();
		checkDuckMethods();
		addObjectMethods();
	}

	private void checkClass() {
		updateCanAdapt(resolveAdapter(getOriginalClass(), getDuckInterface(),
				collectCheckers(getDuckInterface())));
	}

	private InvocationAdapter checkDuckMethod(Method duckMethod) {
		Map<Checker<Annotation>, Annotation> methodCheckers = collectCheckers(duckMethod);
		InvocationAdapter ret = initialForDuckMethod(duckMethod);
		int minToPass = Checkers.getMinPriorityToPass(methodCheckers);
		for (AnnotatedElement element : getRelevantElements(getOriginalClass())) {
			final InvocationAdapter adapter = resolveAdapter(element,
					duckMethod, methodCheckers);
			ret = mergeAdaptersFromElements(ret, adapter);
			if (ret.getPriority() >= minToPass) {
				return ret;
			}
		}
		return ret;
	}

	private InvocationAdapter initialForDuckMethod(Method duckMethod) {
		return safe(adapters.get(duckMethod), MIN);
	}

	private void addObjectMethods() {
		if (cannotBeAdaptedAnyway()) {
			return;
		}
		for (Method m : Object.class.getMethods()) {
			if (!adapters.containsKey(m)) {
				adapters.put(m, new MethodAdapter(m, m));
			}
		}
	}

	private Map<Checker<Annotation>, Annotation> copy(
			Map<Checker<Annotation>, Annotation> checkersMap) {
		return new LinkedHashMap<Checker<Annotation>, Annotation>(checkersMap);
	}

	private final InvocationAdapter resolveAdapter(AnnotatedElement original,
			AnnotatedElement duck,
			Map<Checker<Annotation>, Annotation> checkersMap) {
		Map<Checker<Annotation>, Annotation> checkers = copy(checkersMap);
		int minPriority = getMinPriorityToFail(checkers);
		InvocationAdapter ret = initialForElement(duck);
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

	private InvocationAdapter initialForElement(AnnotatedElement duck) {
		return safe(adapters.get(duck), MAX);
	}

	private Iterable<Method> getDuckMethods() {
		return asList(getDuckInterface().getMethods());
	}

	private void checkDuckMethods() {
		if (cannotBeAdaptedAnyway()) {
			return;
		}
		for (Method duckMethod : getDuckMethods()) {
			InvocationAdapter adapter = checkDuckMethod(duckMethod);
			updateCanAdapt(adapter);
			adapters.put(duckMethod, adapter);
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
		return getDuckInterface().cast(
				Proxy.newProxyInstance(getClass().getClassLoader(),
						new Class[] { getDuckInterface() }, this));
	}

	private D createProxy(O instance) {
		return getDuckInterface().cast(
				Proxy.newProxyInstance(getClass().getClassLoader(),
						new Class[] { getDuckInterface() },
						new AdaptedImpl<O, D>(instance, this)));
	}

}
