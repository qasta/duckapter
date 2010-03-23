package org.duckapter.annotation.checker;

import static org.duckapter.adapted.MethodAdapters.withHighestPriority;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.adapted.MethodAdapter;
import org.duckapter.annotation.Factory;
import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.MethodsOnlyChecker;
import org.duckapter.checker.NameChecker;
import org.duckapter.modifier.checker.StaticChecker;

public class FactoryChecker extends DefaultChecker<Factory> {

	private static final StaticChecker staticChecker = new StaticChecker();
	private static final FieldChecker fieldChecker = new FieldChecker();
	private static final ConstructorChecker constructorChecker = new ConstructorChecker();
	private static final NameChecker<Annotation> nameChecker = new NameChecker<Annotation>();

	@Override
	protected boolean doesCheckClass(Factory anno) {
		return false;
	}

	@Override
	protected MethodAdapter adaptField(Factory anno, Field field,
			Method duckMethod) {
		return withHighestPriority(
				staticChecker.check(null, field, duckMethod), fieldChecker
						.check(null, field, duckMethod), nameChecker.check(
						null, field, duckMethod));
	}

	@Override
	public MethodAdapter adaptConstructor(Factory anno,
			Constructor<?> constructor, Method duckMethod) {
		return constructorChecker.check(null, constructor, duckMethod);
	}

	@Override
	protected MethodAdapter adaptMethod(Factory anno, Method method,
			Method duckMethod) {
		return withHighestPriority(staticChecker
				.check(null, method, duckMethod), nameChecker.check(null,
				method, duckMethod));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends DefaultChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class, NameChecker.class);
	}

}
