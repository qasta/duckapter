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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.AdaptedClass;
import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.MethodAdapter;
import org.duckapter.checker.Checkers;

final class AdaptedClassImpl<O, D> extends AbstractAdaptedClass<O, D> implements
		AdaptedClass<O, D> {

	private final Map<Method, InvocationAdapter> adapters = new HashMap<Method, InvocationAdapter>();

	AdaptedClassImpl(Class<O> originalClass, Class<D> duckInterface) {
		super(duckInterface, originalClass);
		init();
	}

	public Collection<Method> getUnimplementedForClass() {
		Collection<Method> methods = new ArrayList<Method>();
		for (Entry<Method, InvocationAdapter> entry : adapters.entrySet()) {
			if (!entry.getValue().isInvocableOnClass()
					&& !Object.class.equals(entry.getKey().getDeclaringClass())) {
				methods.add(entry.getKey());
			}
		}
		return Collections.unmodifiableCollection(methods);
	}

	public Collection<Method> getUnimplementedForInstance() {
		Collection<Method> methods = new ArrayList<Method>();
		for (Entry<Method, InvocationAdapter> entry : adapters.entrySet()) {
			if (!entry.getValue().isInvocableOnInstance()) {
				methods.add(entry.getKey());
			}
		}
		return Collections.unmodifiableCollection(methods);
	}

	public Object invoke(O originalInstance, Method duckMethod, Object[] args)
			throws Throwable {
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
		for (Method m : Object.class.getMethods()) {
			if (!adapters.containsKey(m)) {
				adapters.put(m, new MethodAdapter(m, m));
			}
		}
	}

	private Map<Checker<Annotation>, Annotation> copy(
			Map<Checker<Annotation>, Annotation> checkersMap) {
		return new HashMap<Checker<Annotation>, Annotation>(checkersMap);
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
		for (Method duckMethod : getDuckMethods()) {
			InvocationAdapter adapter = checkDuckMethod(duckMethod);
			updateCanAdapt(adapter);
			adapters.put(duckMethod, adapter);
		}
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

}
