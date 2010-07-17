package org.duckapter.adapted;

import static org.duckapter.adapter.InvocationAdapters.MAX;
import static org.duckapter.adapter.InvocationAdapters.MIN;
import static org.duckapter.adapter.InvocationAdapters.safe;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.checker.Checkers;

class DuckTestImpl {

	private final Class<?> duckInterface;

	private Map<Checker<Annotation>, Annotation> classCheckers;
	private EnumMap<ElementType, Map<Method, Map<Checker<Annotation>, Annotation>>> checkers;

	private static final ElementType[] TYPES = new ElementType[] {
			ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD };

	DuckTestImpl(Class<?> duckInterface) {
		this.duckInterface = duckInterface;
		init();
	}

	public Map<Method, InvocationAdapter> collectAdapters(Class<?> theClass) {
		Map<Method, InvocationAdapter> ret = new HashMap<Method, InvocationAdapter>();
		for (ElementType type : TYPES) {
			if (hasAnyCheckersFor(type)) {
				fillAdapters(theClass, type, ret);
			}
		}
		if (duckInterface.getMethods().length != ret.size()) {
			for (Method m : duckInterface.getMethods()) {
				if (!ret.containsKey(m)) {
					ret.put(m, InvocationAdapters.NULL);
				}
			}
		}
		return Collections.unmodifiableMap(ret);
	}

	private Iterable<? extends AnnotatedElement> getRelevantElement(
			ElementType type, Class<?> theClass) {
		switch (type) {
		case FIELD:
			return getRelevantField(theClass);
		case CONSTRUCTOR:
			return getRelevantConstructors(theClass);
		case METHOD:
			return getRelevantMethods(theClass);
		default:
			throw new IllegalArgumentException("Illegal element type used: "
					+ type);
		}
	}

	public InvocationAdapter checkClass(Class<?> theClass) {
		if (!hasAnyClassCheckers()) {
			return InvocationAdapters.OK;
		}
		return resolveAdapter(theClass, duckInterface, classCheckers);
	}

	private void init() {
		collectClassCheckers();
		collectMethodsChecker();
	}

	private void collectClassCheckers() {
		classCheckers = Checkers.collectCheckers(duckInterface);
	}

	private void collectMethodsChecker() {
		checkers = new EnumMap<ElementType, Map<Method, Map<Checker<Annotation>, Annotation>>>(
				ElementType.class);
		for (ElementType type : TYPES) {
			checkers
					.put(
							type,
							new HashMap<Method, Map<Checker<Annotation>, Annotation>>());
		}
		for (Method m : duckInterface.getMethods()) {
			Map<Checker<Annotation>, Annotation> collected = Checkers
					.collectCheckers(m);
			for (ElementType type : TYPES) {
				if (mightBe(m, type)) {
					checkers.get(type).put(m, collected);
				}
			}
		}
	}

	private InvocationAdapter adaptMethod(
			Class<?> theClass, 
			AnnotatedElement element, 
			Method duckMethod,
			Map<Checker<Annotation>, Annotation> theCheckers,
			Map<Method, InvocationAdapter> adapters) {
		if (theCheckers == null) {
			return InvocationAdapters.NULL;
		}
		return resolveAdapter(
				theClass, 
				element, 
				duckMethod, 
				theCheckers,
				adapters);
	}

	private void fillAdapters(Class<?> theClass, ElementType elementType,
			Map<Method, InvocationAdapter> adapters) {
		for (Entry<Method, Map<Checker<Annotation>, Annotation>> entry : checkers
				.get(elementType).entrySet()) {
			final Method duckMethod = entry.getKey();
			InvocationAdapter ret = initialForDuckMethod(duckMethod, adapters);
			for (AnnotatedElement element : getRelevantElement(elementType,
					theClass)) {
				final Map<Checker<Annotation>, Annotation> theCheckers = entry
						.getValue();
				final InvocationAdapter adapter 
					= adaptMethod(
							theClass,
							element, 
							duckMethod, 
							theCheckers, 
							adapters);
				ret = mergeAdaptersFromElements(ret, adapter);
			}
		}

	}

	private InvocationAdapter mergeAdaptersFromElements(InvocationAdapter ret,
			final InvocationAdapter adapter) {
		if (ret.getPriority() >= adapter.getPriority()) {
			return ret.orMerge(adapter);
		}
		return adapter.orMerge(ret);
	}

	private Collection<Constructor<?>> getRelevantConstructors(Class<?> theClass) {
		return AdaptedClassHelper.getRelevantConstructors(theClass);
	}

	private Collection<Field> getRelevantField(Class<?> theClass) {
		return AdaptedClassHelper.getRelevantFields(theClass);
	}

	private Collection<Method> getRelevantMethods(Class<?> theClass) {
		return AdaptedClassHelper.getRelevantMethods(theClass);
	}

	private boolean hasAnyClassCheckers() {
		return !classCheckers.isEmpty();
	}

	private boolean hasAnyCheckersFor(ElementType type) {
		return !checkers.get(type).isEmpty();
	}

	private boolean mightBe(final Annotation anno, final ElementType elementType) {
		if (ElementType.METHOD == elementType) {
			return true;
		}
		if (!Checkers.isCheckerAnnotation(anno)) {
			return true;
		}
		Class<? extends Annotation> aType = anno.annotationType();
		if (!aType.isAnnotationPresent(Target.class)) {
			return true;
		}
		return Arrays.asList(aType.getAnnotation(Target.class)).contains(
				elementType);
	}

	private boolean mightBe(final Method duckMethod,
			final ElementType elementType) {
		for (Annotation anno : duckMethod.getAnnotations()) {
			if (!mightBe(anno, elementType)) {
				return false;
			}
		}
		return true;
	}
	
	private final InvocationAdapter resolveAdapter(Class<?> theClass,
			Class<?> duck, Map<Checker<Annotation>, Annotation> theCheckers) {
		InvocationAdapter ret = InvocationAdapters.MAX;
		for (Entry<Checker<Annotation>, Annotation> entry : theCheckers.entrySet()) {
			final Checker<Annotation> ch = entry.getKey();
			final Annotation anno = entry.getValue();
			if (Checkers.canAdapt(anno, theClass)) {
				final InvocationAdapter adapter = ch.adapt(anno, theClass,
						duck, theClass);
				ret = mergeAdaptersFromCheckers(ret, adapter);
			}
		}
		return ret;
	}

	private final InvocationAdapter resolveAdapter(Class<?> theClass,
			AnnotatedElement original, Method duck,
			Map<Checker<Annotation>, Annotation> checkers,
			Map<Method, InvocationAdapter> adapters) {
		InvocationAdapter ret = initialForElement(duck, adapters);
		for (Entry<Checker<Annotation>, Annotation> entry : checkers.entrySet()) {
			final Checker<Annotation> ch = entry.getKey();
			final Annotation anno = entry.getValue();
			if (Checkers.canAdapt(anno, original)) {
				final InvocationAdapter adapter = ch.adapt(anno, original,
						duck, theClass);
				ret = mergeAdaptersFromCheckers(ret, adapter);
			}
		}
		return ret;
	}

	private InvocationAdapter mergeAdaptersFromCheckers(InvocationAdapter ret,
			final InvocationAdapter adapter) {
		if (ret.getPriority() >= adapter.getPriority()) {
			return ret.andMerge(adapter);
		}
		return adapter.andMerge(ret);
	}

	private InvocationAdapter initialForElement(Object duck,
			Map<Method, InvocationAdapter> adapters) {
		return safe(adapters.get(duck), MAX);
	}

	private InvocationAdapter initialForDuckMethod(Object duck,
			Map<Method, InvocationAdapter> adapters) {
		return safe(adapters.get(duck), MIN);
	}

}
