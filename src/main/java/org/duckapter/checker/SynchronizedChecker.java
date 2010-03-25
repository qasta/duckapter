package org.duckapter.checker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.duckapter.annotation.Synchronized;

public class SynchronizedChecker extends DefaultChecker<Synchronized> {

	@Override
	protected boolean doesCheckClass(Synchronized anno) {
		return false;
	}

	@Override
	protected boolean doesCheckField(Synchronized anno) {
		return false;
	}

	@Override
	protected boolean checkMethod(Synchronized anno, Method method,
			Method duckMethod) {
		return Modifier.isSynchronized(method.getModifiers());
	}

	@Override
	protected boolean doesCheckConstructor(Synchronized anno) {
		return false;
	}
}
