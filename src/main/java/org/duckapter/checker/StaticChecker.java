package org.duckapter.checker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.annotation.Static;

public class StaticChecker extends DefaultChecker<Static> {

	@Override
	protected boolean doesCheckClass(Static a) {
		return false;
	}

	@Override
	protected boolean checkField(Static a, Field field, Method duckMethod) {
		return Modifier.isStatic(field.getModifiers());
	}

	@Override
	protected boolean checkMethod(Static a, Method method, Method duckMethod) {
		return Modifier.isStatic(method.getModifiers());
	}

}
