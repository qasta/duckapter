package org.duckapter.modifier.checker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.checker.DefaultChecker;
import org.duckapter.modifier.Abstract;

public class AbstractChecker extends DefaultChecker<Abstract> {

	@Override
	public boolean checkClass(Abstract a, Class<?> clazz, Class<?> duckInterface) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	@Override
	protected boolean checkMethod(Abstract a, Method method, Method duckMethod) {
		return Modifier.isAbstract(method.getModifiers());
	}
}
