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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.duckapter.AdaptationViolation;
import org.duckapter.AdaptedClass;
import org.duckapter.Checker;
import org.duckapter.DuckAnnotation;
import org.duckapter.checker.DefaultChecker;

final class AdaptedClassImpl implements AdaptedClass {

	private final Map<Method, MethodAdapter> adapters = new HashMap<Method, MethodAdapter>();
	private final Collection<AdaptationViolation> adaptationViolations = new ArrayList<AdaptationViolation>();

	private final Class<?> originalClass;
	private final Class<?> duckInterface;

	private boolean canAdaptClass;
	private boolean canAdaptInstance;

	AdaptedClassImpl(Class<?> originalClass, Class<?> duckInterface) {
		this.originalClass = originalClass;
		this.duckInterface = duckInterface;
		init();
	}

	private final void init() {
		MethodAdapter adapter = doCheck(originalClass, duckInterface,
				collectCheckers(duckInterface));
		canAdaptClass = MethodAdapters.notNull(adapter);
		canAdaptInstance = MethodAdapters.notNull(adapter);

		for (Method duckMethod : duckInterface.getMethods()) {
			checkDuckMethod(duckMethod);
		}
	}

	private final void checkDuckMethod(
			Method duckMethod) {
		boolean okForClass = false;
		boolean okForInstance = false;

		Set<Checker<?>> methodCheckers = collectCheckers(duckMethod);

		for (AnnotatedElement element : getRelevantElements()) {
			final MethodAdapter adapter = doCheck(element, duckMethod,
					methodCheckers);
			MethodAdapter old = MethodAdapters.safe(adapters.get(duckMethod));
			adapters.put(duckMethod,adapter.mergeWith(old));
			
			if (MethodAdapters.notNull(adapter)) {
				okForInstance |= true;
				okForClass |= isStatic(element);
			}
		}

		if (MethodAdapters.isNull(adapters.get(duckMethod))) {
			canAdaptClass = false;
			canAdaptInstance = false;
			return;
		}
		canAdaptClass &= okForClass;
		canAdaptInstance &= okForInstance;
	}

	@SuppressWarnings("unchecked")
	private final MethodAdapter doCheck(AnnotatedElement original,
			AnnotatedElement duck, Set<Checker<?>> theCheckers) {
		AdaptationViolation vio = null;
		Set<Checker<?>> checkers = new HashSet<Checker<?>>(theCheckers);
		MethodAdapter ret = MethodAdapters.safe(adapters.get(duck));
		for (Annotation anno : duck.getAnnotations()) {
			if (anno.annotationType().isAnnotationPresent(DuckAnnotation.class)) {
				Checker ch = getCheckerInstance(anno.annotationType()
						.getAnnotation(DuckAnnotation.class).value());
				if (checkers.contains(ch) && ch.doesCheck(anno, original)) {
					final MethodAdapter adapter = ch
							.check(anno, original, duck);
					if (MethodAdapters.isNull(adapter)) {
						vio = new AdaptationViolation(duck, original, ch
								.getClass());
						break;
					}
					ret = ret.mergeWith(adapter);
					checkers.remove(ch);
				}
			}
		}
		if (vio != null) {
			adaptationViolations.add(vio);
			return MethodAdapters.NULL;
		}
		for (Checker<?> checker : checkers) {
			if (checkers.contains(checker) && checker.doesCheck(null, original)) {
				final MethodAdapter adapter = checker.check(null, original,
						duck);
				ret = ret.mergeWith(adapter);
				if (MethodAdapters.isNull(adapter)) {
					vio = new AdaptationViolation(duck, original, checker
							.getClass());
					break;
				}
			}
		}
		if (vio != null) {
			adaptationViolations.add(vio);
			return MethodAdapters.NULL;
		}
		return ret;
	}

	private Collection<AnnotatedElement> getRelevantElements() {
		Collection<AnnotatedElement> elements = new ArrayList<AnnotatedElement>();
		elements.addAll(getRelevantConstructors(originalClass));
		elements.addAll(getRelevantFields(originalClass));
		elements.addAll(getRelevantMethods(originalClass));
		return elements;
	}

	@SuppressWarnings("unchecked")
	private boolean isStatic(AnnotatedElement element) {
		if (element instanceof Constructor) {
			return true;
		}
		if (element instanceof Method) {
			return Modifier.isStatic(((Method) element).getModifiers());
		}
		if (element instanceof Field) {
			return Modifier.isStatic(((Field) element).getModifiers());
		}
		return false;

	}

	private static Collection<Method> getRelevantMethods(Class<?> original) {
		// TODO: vymyslet, jak dedit
		return Arrays.asList(original.getDeclaredMethods());
	}

	private static Collection<java.lang.reflect.Constructor<?>> getRelevantConstructors(
			Class<?> original) {
		// TODO: vymyslet, jak dedit
		return Arrays.asList(original.getDeclaredConstructors());
	}

	private static Collection<Field> getRelevantFields(Class<?> original) {
		// TODO: vymyslet, jak dedit
		return Arrays.asList(original.getDeclaredFields());
	}

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

	private static Map<Class<?>, Checker<?>> checkerClassToInstanceMap = new HashMap<Class<?>, Checker<?>>();

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

	@Override
	public boolean canAdaptClass() {
		return canAdaptClass;
	}

	@Override
	public boolean canAdaptInstance() {
		return canAdaptInstance;
	}

	@Override
	public Class<?> getDuckInterface() {
		return duckInterface;
	}

	@Override
	public Class<?> getOriginalClass() {
		return originalClass;
	}

	@Override
	public Collection<AdaptationViolation> getAdaptationViolations() {
		return new ArrayList<AdaptationViolation>(adaptationViolations);
	}

	@Override
	public Object invoke(Object originalInstance, Method duckMethod,
			Object[] args) throws Throwable {
		return adapters.get(duckMethod).invoke(originalInstance, args);
	}

}
