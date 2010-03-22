package org.duckapter.modifier.checker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.checker.DefaultChecker;
import org.duckapter.modifier.Native;

public class NativeChecker extends DefaultChecker<Native> {
	@Override
	protected boolean checkMethod(Native n, Method method, Method duckMethod) {
		return Modifier.isNative(method.getModifiers());
	}

	@Override
	protected boolean doesCheckClass(Native anno) {
		return false;
	}
}
