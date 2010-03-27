package org.duckapter.adapted;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.AdaptedClass;
import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.checker.Checkers;

final class AdaptedClassImpl<O,D> extends AbstractAdaptedClass<O,D> implements AdaptedClass<O,D> {


	private static Map<String, Field> getFields(Class<?> clazz,
			boolean exceptPrivate) {
		Map<String, Field> ret = new LinkedHashMap<String, Field>();
		for (Field f : clazz.getDeclaredFields()) {
			if (!exceptPrivate || !Modifier.isPrivate(f.getModifiers())) {
				ret.put(f.getName(), f);
			}
		}
		return ret;
	}



	private static Collection<Constructor<?>> getRelevantConstructors(
			Class<?> original) {
		return Arrays.asList(original.getDeclaredConstructors());
	}
	
	private static Map<MethodSignature, Method> getMethods(Class<?> clazz,
			boolean exceptPrivate) {
		Map<MethodSignature, Method> ret = new LinkedHashMap<MethodSignature, Method>();
		for (Method m : clazz.getDeclaredMethods()) {
			if (!exceptPrivate || !Modifier.isPrivate(m.getModifiers())) {
				ret
				.put(new MethodSignature(m.getParameterTypes(), m
						.getName()), m);
			}
		}
		return ret;
	}

	private static Collection<Field> getRelevantFields(Class<?> original) {
		Map<String, Field> fields = new LinkedHashMap<String, Field>();
		for (Class<?> c : getSuperClasses(original)) {
			fields.putAll(getFields(c, true));
		}
		for (Field f : original.getDeclaredFields()) {
			fields.put(f.getName(), f);
		}
		return fields.values();
	}

	private static Collection<Method> getRelevantMethods(Class<?> original) {
		Map<MethodSignature, Method> methods = new LinkedHashMap<MethodSignature, Method>();
		for (Class<?> c : getSuperClasses(original)) {
			methods.putAll(getMethods(c, true));
		}
		for (Method m : original.getDeclaredMethods()) {
			methods.put(
					new MethodSignature(m.getParameterTypes(), m.getName()), m);
		}
		return methods.values();
	}

	private static List<Class<?>> getSuperClasses(Class<?> clazz) {
		List<Class<?>> ret = new ArrayList<Class<?>>();
		if (clazz.getSuperclass() != null) {
			ret.addAll(getSuperClasses(clazz.getSuperclass()));
			ret.add(clazz.getSuperclass());
		}
		return ret;
	}

	private final Map<Method, InvocationAdapter> adapters = new HashMap<Method, InvocationAdapter>();

	AdaptedClassImpl(Class<O> originalClass, Class<D> duckInterface) {
		super(duckInterface, originalClass);
		init();
	}

	@Override
	public Object invoke(O originalInstance, Method duckMethod,
			Object[] args) throws Throwable {
		return adapters.get(duckMethod).invoke(originalInstance, args);
	}

	@SuppressWarnings("unchecked")
	private final InvocationAdapter doCheck(AnnotatedElement original,
			AnnotatedElement duck, Map<Checker<Annotation>, Annotation> checkersMap) {
		Map<Checker<Annotation>, Annotation> checkers = new HashMap<Checker<Annotation>, Annotation>(checkersMap);
		InvocationAdapter ret = InvocationAdapters.MAX;
		for (Annotation anno : duck.getAnnotations()) {
			Checker ch = Checkers.getChecker(anno);
			if (ch == null) {
				continue;
			}
			if (checkers.containsKey(ch) && ch.canAdapt(anno, original)) {
				final InvocationAdapter adapter = ch.adapt(anno, original, duck);
				ret = ret.andMerge(adapter);
				checkers.remove(ch);
			}
		}
		for (Entry<Checker<Annotation>, Annotation> entry : checkers.entrySet()) {
			if (checkers.containsKey(entry.getKey()) && entry.getKey().canAdapt(entry.getValue(), original)) {
				final InvocationAdapter adapter = entry.getKey().adapt(entry.getValue(), original,
						duck);
				ret = ret.andMerge(adapter);
			}
		}
		return ret;
	}



	private Collection<AnnotatedElement> getRelevantElements() {
		Collection<AnnotatedElement> elements = new ArrayList<AnnotatedElement>();
		elements.addAll(getRelevantConstructors(getOriginalClass()));
		elements.addAll(getRelevantFields(getOriginalClass()));
		elements.addAll(getRelevantMethods(getOriginalClass()));
		return elements;
	}

	private void init() {
		InvocationAdapter adapter = doCheck(getOriginalClass(), getDuckInterface(),
				Checkers.collectCheckers(getDuckInterface()));
		canAdaptClass = adapter.isInvocableOnClass();
		canAdaptInstance = adapter.isInvocableOnInstance();

		for (Method duckMethod : getDuckInterface().getMethods()) {
			InvocationAdapter old = checkDuckMethod(duckMethod);

			canAdaptClass = canAdaptClass && old.isInvocableOnClass();
			canAdaptInstance = canAdaptInstance && old.isInvocableOnInstance();
			adapters.put(duckMethod, old);
		}
	}

	private InvocationAdapter checkDuckMethod(Method duckMethod) {
		Map<Checker<Annotation>, Annotation> methodCheckers = Checkers.collectCheckers(duckMethod);
		InvocationAdapter adapter = InvocationAdapters.MIN;
		for (AnnotatedElement element : getRelevantElements()) {
			adapter = doCheck(element, duckMethod, methodCheckers).orMerge(
					adapter);
		}
		return adapter;
	}

	@Override
	public Collection<Method> getUnimplementedForClass() {
		Collection<Method> methods = new ArrayList<Method>();
		for (Entry<Method, InvocationAdapter> entry : adapters.entrySet()) {
			if (!entry.getValue().isInvocableOnClass()) {
				methods.add(entry.getKey());
			}
		}
		return Collections.unmodifiableCollection(methods);
	}

	@Override
	public Collection<Method> getUnimplementedForInstance() {
		Collection<Method> methods = new ArrayList<Method>();
		for (Entry<Method, InvocationAdapter> entry : adapters.entrySet()) {
			if (!entry.getValue().isInvocableOnInstance()) {
				methods.add(entry.getKey());
			}
		}
		return Collections.unmodifiableCollection(methods);
	}
	
}
