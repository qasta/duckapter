package org.duckapter.checker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.annotation.Volatile;

public class VolatileChecker extends DefaultChecker<Volatile> {

	@Override
	protected boolean doesCheckClass(Volatile anno) {
		return false;
	}

	@Override
	protected boolean doesCheckConstructor(Volatile anno) {
		return false;
	}

	@Override
	protected boolean checkField(Volatile anno, Field field, Method duckMethod) {
		return Modifier.isVolatile(field.getModifiers());
	}

	@Override
	protected boolean doesCheckMethod(Volatile anno) {
		return false;
	}
}
