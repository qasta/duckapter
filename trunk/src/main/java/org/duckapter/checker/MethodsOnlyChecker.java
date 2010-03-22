package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodsOnlyChecker<T extends Annotation> extends DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return true;
	};

}
