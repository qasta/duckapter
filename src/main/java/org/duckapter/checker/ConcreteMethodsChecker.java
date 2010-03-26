package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

public class ConcreteMethodsChecker<T extends Annotation> extends
		BooleanCheckerBase<T> {

	@Override
	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return !Modifier.isAbstract(method.getModifiers());
	};

	@Override
	protected boolean checkConstructor(T anno, Constructor<?> con,
			Method duckMethod) {
		return !Modifier.isAbstract(con.getModifiers());
	};

	@Override
	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno != null) {
			return super.getTargetElements(anno);
		}
		return Arrays.asList(new ElementType[] { ElementType.METHOD,
				ElementType.CONSTRUCTOR });
	};
}
