package org.duckapter.annotation.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Constructor;
import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.MethodsOnlyChecker;

public class ConstructorChecker extends DefaultChecker<Constructor> {

	@Override
	protected boolean doesCheckClass(Constructor anno) {
		return false;
	}

	@Override
	public boolean checkConstructor(Constructor anno,
			java.lang.reflect.Constructor<?> constructor, Method duckMethod) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<MethodsOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class);
	}

}
