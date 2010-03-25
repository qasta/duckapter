package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.GetFieldAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.adapter.SetFieldAdapter;
import org.duckapter.annotation.Field;

public class FieldChecker extends DefaultChecker<Field> {

	@Override
	protected boolean doesCheckClass(Field anno) {
		return false;
	}

	@Override
	protected boolean checkConstructor(Field anno, Constructor<?> constructor,
			Method duckMethod) {
		return false;
	}

	@Override
	protected MethodAdapter adaptField(Field anno, java.lang.reflect.Field field,
			Method duckMethod) {
		if (duckMethod.getParameterTypes().length == 1 && !Modifier.isFinal(field.getModifiers())) {
			return new SetFieldAdapter(duckMethod, field);
		} else if (duckMethod.getParameterTypes().length == 0) {
			return new GetFieldAdapter(duckMethod, field);
		}
		return MethodAdapters.NULL;
	}

	@Override
	protected boolean checkMethod(Field anno, Method method, Method duckMethod) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<MethodsOnlyChecker>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Arrays.asList(MethodsOnlyChecker.class);
	}

}
