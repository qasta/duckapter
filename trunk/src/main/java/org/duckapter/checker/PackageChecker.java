package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Package;

public class PackageChecker extends DefaultChecker<Package> {

	@Override
	public boolean checkClass(Package anno, Class<?> clazz,
			Class<?> duckInterface) {
		return anno.value().checkPackage(clazz.getModifiers());
	}

	@Override
	protected boolean checkField(Package anno, Field field, Method duckMethod) {
		return anno.value().checkPackage(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(Package anno, Method method, Method duckMethod) {
		return anno.value().checkPackage(method.getModifiers());
	}

	@Override
	public boolean checkConstructor(Package anno, Constructor<?> c,
			Method duckMethod) {
		return anno.value().checkPackage(c.getModifiers());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<PublicOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(PublicOnlyChecker.class);
	}
}
