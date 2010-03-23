package org.duckapter.annotation.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.adapted.ConstructorAdapter;
import org.duckapter.adapted.MethodAdapter;
import org.duckapter.adapted.MethodAdapters;
import org.duckapter.annotation.Constructor;
import org.duckapter.checker.DefaultChecker;
import org.duckapter.checker.MethodsOnlyChecker;
import org.duckapter.checker.NameChecker;

public class ConstructorChecker extends DefaultChecker<Constructor> {

	@Override
	protected boolean doesCheckClass(Constructor anno) {
		return false;
	}

	@Override
	public MethodAdapter adaptConstructor(Constructor anno,
			java.lang.reflect.Constructor<?> constructor, Method duckMethod) {
		try {
			constructor.setAccessible(true);
		} catch (SecurityException e) {
			return MethodAdapters.NULL;
		}
		return new ConstructorAdapter(duckMethod, constructor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends DefaultChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class, NameChecker.class);
	}

}
