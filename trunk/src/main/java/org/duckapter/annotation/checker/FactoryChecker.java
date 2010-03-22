package org.duckapter.annotation.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Factory;
import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.MethodsOnlyChecker;
import org.duckapter.modifier.checker.StaticChecker;

public class FactoryChecker extends DefaultChecker<Factory> {

	private static final StaticChecker staticChecker = new StaticChecker();
	private static final FieldChecker fieldChecker = new FieldChecker();

	@Override
	protected boolean doesCheckClass(Factory anno) {
		return false;
	}

	@Override
	protected boolean checkField(Factory anno, Field field, Method duckMethod) {
		return staticChecker.check(null, field, duckMethod)
				&& fieldChecker.check(null, field, duckMethod);
	}

	@Override
	public boolean checkConstructor(Factory anno, Constructor<?> constructor,
			Method duckMethod) {
		return true;
	}

	@Override
	protected boolean checkMethod(Factory anno, Method method, Method duckMethod) {
		return staticChecker.check(null, method, duckMethod)
				&& fieldChecker.check(null, method, duckMethod);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<MethodsOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class);
	}

}
