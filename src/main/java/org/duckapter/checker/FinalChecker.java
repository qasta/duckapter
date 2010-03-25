package org.duckapter.checker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.annotation.Final;

public class FinalChecker extends DefaultChecker<Final> {

	@Override
	public boolean checkClass(Final f, Class<?> clazz, Class<?> duckInterface) {
		return Modifier.isFinal(clazz.getModifiers());
	}

	@Override
	protected boolean checkField(Final f, Field field, Method duckMethod) {
		return Modifier.isFinal(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(Final f, Method method, Method duckMethod) {
		return Modifier.isFinal(method.getModifiers());
	}

	@Override
	public boolean checkConstructor(Final f, Constructor<?> c, Method duckMethod) {
		return Modifier.isFinal(c.getModifiers());
	}
}
