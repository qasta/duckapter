package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Abstract;

public class AbstractChecker extends DefaultChecker<Abstract> {

	@Override
	public boolean checkClass(Abstract a, Class<?> clazz, Class<?> duckInterface) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	@Override
	protected boolean checkMethod(Abstract a, Method method, Method duckMethod) {
		return Modifier.isAbstract(method.getModifiers());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Class<ConcreteMethodsChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(ConcreteMethodsChecker.class);
	}
}
