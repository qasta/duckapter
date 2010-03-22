package org.duckapter.modifier.checker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.checker.DefaultChecker;
import org.duckapter.modifier.StrictFloatingPoint;

public class StrictFloatingPointChecker extends
		DefaultChecker<StrictFloatingPoint> {

	@Override
	public boolean checkClass(StrictFloatingPoint anno, Class<?> clazz,
			Class<?> duckInterface) {
		return Modifier.isStrict(clazz.getModifiers());
	}

	@Override
	protected boolean doesCheckField(StrictFloatingPoint anno) {
		return false;
	}

	@Override
	protected boolean checkMethod(StrictFloatingPoint anno, Method method,
			Method duckMethod) {
		return Modifier.isStrict(method.getModifiers());
	}

	@Override
	protected boolean doesCheckConstructor(StrictFloatingPoint anno) {
		return false;
	}

}
