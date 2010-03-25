package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConcreteMethodsChecker<T extends Annotation> extends
		DefaultChecker<T> {

	@Override
	protected boolean doesCheckClass(T anno) {
		return false;
	};

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) { 
		return !Modifier.isAbstract(method.getModifiers());
	};
	
	@Override
	protected boolean checkConstructor(T anno, Constructor<?> con, Method duckMethod) { 
		return !Modifier.isAbstract(con.getModifiers());
	};
	
	@Override
	protected boolean doesCheckField(T anno) { return false; };

}
