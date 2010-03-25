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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.duckapter.AdaptedClass;
import org.duckapter.Checker;
import org.duckapter.DuckAnnotation;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.checker.DefaultChecker;

final class AdaptedClassImpl extends AbstractAdaptedClass implements AdaptedClass {

	private static class MethodSignature {
		private final String name;
		private final Class<?>[] parameterTypes;

		public MethodSignature(Class<?>[] parameterTypes, String name) {
			this.parameterTypes = parameterTypes;
			this.name = name;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MethodSignature other = (MethodSignature) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (!Arrays.equals(parameterTypes, other.parameterTypes))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + Arrays.hashCode(parameterTypes);
			return result;
		}

	}

	private static Map<Class<?>, Checker<?>> checkerClassToInstanceMap = new HashMap<Class<?>, Checker<?>>();

	private static Set<Checker<?>> collectCheckers(AnnotatedElement m) {
		Set<Checker<?>> methodCheckers = new HashSet<Checker<?>>(
				getDefaultCheckers());
		for (Annotation anno : m.getAnnotations()) {
			if (anno.annotationType().isAnnotationPresent(DuckAnnotation.class)) {
				methodCheckers.add(getCheckerInstance(anno.annotationType()
						.getAnnotation(DuckAnnotation.class).value()));
			}
		}
		Set<Checker<?>> suppressedMethodChecker = new HashSet<Checker<?>>();
		for (Checker<?> checker : methodCheckers) {
			for (Class<Checker<Annotation>> clazz : checker.suppressCheckers(m)) {
				suppressedMethodChecker.add(getCheckerInstance(clazz));
			}
		}
		methodCheckers.removeAll(suppressedMethodChecker);
		return methodCheckers;
	}

	private static Collection<? extends Checker<?>> getDefaultCheckers() {
		return DefaultChecker.getDefaultCheckers();
	}

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

	@SuppressWarnings("unchecked")
	private static <A extends Annotation> Checker<A> getCheckerInstance(
			Class<? extends Checker<? extends Annotation>> theClass) {
		Checker<A> checker = (Checker<A>) checkerClassToInstanceMap
				.get(theClass);
		if (checker == null) {
			try {
				checker = (Checker<A>) theClass.newInstance();
				checkerClassToInstanceMap.put(theClass, checker);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return checker;
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

	private static Collection<Constructor<?>> getRelevantConstructors(
			Class<?> original) {
		return Arrays.asList(original.getDeclaredConstructors());
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

	private final Map<Method, MethodAdapter> adapters = new HashMap<Method, MethodAdapter>();

	AdaptedClassImpl(Class<?> originalClass, Class<?> duckInterface) {
		super(duckInterface, originalClass);
		init();
	}

	@Override
	public Object invoke(Object originalInstance, Method duckMethod,
			Object[] args) throws Throwable {
		return adapters.get(duckMethod).invoke(originalInstance, args);
	}

	@SuppressWarnings("unchecked")
	private final MethodAdapter doCheck(AnnotatedElement original,
			AnnotatedElement duck, Set<Checker<?>> theCheckers) {
		Set<Checker<?>> checkers = new HashSet<Checker<?>>(theCheckers);
		MethodAdapter ret = MethodAdapters.MAX;
		for (Annotation anno : duck.getAnnotations()) {
			Checker ch = getChecker(anno);
			if (ch == null) {
				continue;
			}
			if (checkers.contains(ch) && ch.doesCheck(anno, original)) {
				final MethodAdapter adapter = ch.check(anno, original, duck);
				ret = ret.andMerge(adapter);
				checkers.remove(ch);
			}
		}
		for (Checker<?> checker : checkers) {
			if (checkers.contains(checker) && checker.doesCheck(null, original)) {
				final MethodAdapter adapter = checker.check(null, original,
						duck);
				ret = ret.andMerge(adapter);
			}
		}
		return ret;
	}

	private boolean isDuckAnnotation(Annotation anno) {
		return anno.annotationType().isAnnotationPresent(DuckAnnotation.class);
	}

	@SuppressWarnings("unchecked")
	private Checker getChecker(Annotation anno) {
		if (!isDuckAnnotation(anno)) {
			return null;
		}
		Checker ch = getCheckerInstance(anno.annotationType().getAnnotation(
				DuckAnnotation.class).value());
		return ch;
	}

	private Collection<AnnotatedElement> getRelevantElements() {
		Collection<AnnotatedElement> elements = new ArrayList<AnnotatedElement>();
		elements.addAll(getRelevantConstructors(originalClass));
		elements.addAll(getRelevantFields(originalClass));
		elements.addAll(getRelevantMethods(originalClass));
		return elements;
	}

	private void init() {
		MethodAdapter adapter = doCheck(originalClass, duckInterface,
				collectCheckers(duckInterface));
		canAdaptClass = MethodAdapters.notNull(adapter);
		canAdaptInstance = MethodAdapters.notNull(adapter);

		for (Method duckMethod : duckInterface.getMethods()) {
			MethodAdapter old = checkDuckMethod(duckMethod);

			canAdaptClass &= old.isInvocableOnClass();
			canAdaptInstance &= old.isInvocableOnInstance();
			adapters.put(duckMethod, old);
		}
	}

	private MethodAdapter checkDuckMethod(Method duckMethod) {
		Set<Checker<?>> methodCheckers = collectCheckers(duckMethod);
		MethodAdapter adapter = MethodAdapters.MIN;
		for (AnnotatedElement element : getRelevantElements()) {
			adapter = doCheck(element, duckMethod, methodCheckers).orMerge(
					adapter);
		}
		return adapter;
	}

	@Override
	public Collection<Method> getUnimplementedForClass() {
		Collection<Method> methods = new ArrayList<Method>();
		for (Entry<Method, MethodAdapter> entry : adapters.entrySet()) {
			if (!entry.getValue().isInvocableOnClass()) {
				methods.add(entry.getKey());
			}
		}
		return Collections.unmodifiableCollection(methods);
	}

	@Override
	public Collection<Method> getUnimplementedForInstance() {
		Collection<Method> methods = new ArrayList<Method>();
		for (Entry<Method, MethodAdapter> entry : adapters.entrySet()) {
			if (!entry.getValue().isInvocableOnInstance()) {
				methods.add(entry.getKey());
			}
		}
		return Collections.unmodifiableCollection(methods);
	}
}
