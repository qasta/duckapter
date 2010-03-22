package org.duckapter.adapted;

import static org.duckapter.adapted.MethodAdapterFactory.createMethodAdapter;

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
		// now we can only duck interface!
		if (!duckInterface.isInterface()) {
			canAdaptClass = false;
			canAdaptInstance = false;
			for (Method m : duckInterface.getMethods()) {
				adapters.put(m, NullAdapter.INSTANCE);
			}
			return;
		}

		AdaptationViolation vio = doCheck(originalClass, duckInterface,
				collectCheckers(duckInterface));
		if (vio!= null) {
			adaptationViolations.add(vio);
			canAdaptClass = false;
			canAdaptInstance = false;
		} else {
			// default is true
			canAdaptClass = true;
			canAdaptInstance = true;
		}

		for (Method duckMethod : duckInterface.getMethods()) {
			adaptationViolations.addAll(checkDuckMethod(duckMethod));
		}
	}

	private Collection<AdaptationViolation> checkDuckMethod(Method duckMethod) {
		boolean okForClass = false;
		boolean okForInstance = false;

		Set<Checker<?>> methodCheckers = collectCheckers(duckMethod);
		Collection<AdaptationViolation> vios = new ArrayList<AdaptationViolation>();
		
		for (AnnotatedElement element : getRelevantElements()) {
			AdaptationViolation vio = doCheck(element, duckMethod, methodCheckers);
			if (vio == null) {
				adapters.put(duckMethod, createMethodAdapter(duckMethod,
						element));
				okForInstance |= true;
				okForClass |= isStatic(element);
			} else {
				vios.add(vio);
			}
		}

		if (!adapters.containsKey(duckMethod)) {
			adapters.put(duckMethod, NullAdapter.INSTANCE);
			canAdaptClass = false;
			canAdaptInstance = false;
			return vios;
		}
		canAdaptClass &= okForClass;
		canAdaptInstance &= okForInstance;
		return Collections.emptyList();
		
	}

	@SuppressWarnings("unchecked")
	private static AdaptationViolation doCheck(AnnotatedElement original,
			AnnotatedElement duck, Set<Checker<?>> theCheckers) {

		Set<Checker<?>> checkers = new HashSet<Checker<?>>(theCheckers);

		for (Annotation anno : duck.getAnnotations()) {
			if (anno.annotationType().isAnnotationPresent(DuckAnnotation.class)) {
				Checker ch = getCheckerInstance(anno.annotationType()
						.getAnnotation(DuckAnnotation.class).value());
				if (checkers.contains(ch) && ch.doesCheck(anno, original)) {
					if (!ch.check(anno, original, duck)) {
						return new AdaptationViolation(duck, original, ch
								.getClass());
					}
					checkers.remove(ch);
				}
			}
		}

		for (Checker<?> checker : checkers) {
			if (checkers.contains(checker) && checker.doesCheck(null, original)) {
				if (!checker.check(null, original, duck)) {
					return new AdaptationViolation(duck, original, checker
							.getClass());
				}
			}
		}

		return null;
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
