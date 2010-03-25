package org.duckapter.checker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.annotation.Transient;

public class TransientChecker extends DefaultChecker<Transient> {

	@Override
	protected boolean doesCheckClass(Transient t) {
		return false;
	}

	@Override
	protected boolean checkField(Transient n, Field field, Method duckMethod) {
		return Modifier.isTransient(field.getModifiers());
	}

	@Override
	protected boolean doesCheckMethod(Transient t) {
		return false;
	}

	@Override
	protected boolean doesCheckConstructor(Transient anno) {
		return false;
	}
}
