package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.duckapter.annotation.Alias;

/**
 * Checker for the {@link Alias} annotation. Suppresses {@link NameChecker}.
 * @author Vladimir Orany
 * @see Alias
 */
public class AliasChecker extends NameChecker<Alias> {

	@Override
	protected boolean checkField(Alias anno, Field field, Method duckMethod, Class<?> classOfOriginal) {
		if (super.checkField(anno, field, duckMethod, classOfOriginal)) {
			return true;
		}
		for (String alias : anno.value()) {
			if (checkFieldName(alias, field.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean checkMethod(Alias anno, Method method, Method duckMethod, Class<?> classOfOriginal) {
		if (super.checkMethod(anno, method, duckMethod, classOfOriginal)) {
			return true;
		}
		for (String alias : anno.value()) {
			if (checkMethodName(alias, method.getName())
					|| checkMethodName("is" + alias, method.getName())
					|| checkMethodName("get" + alias, method.getName())
					|| checkMethodName("set" + alias, method.getName())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<NameChecker>> suppressCheckers(Alias anno,
			AnnotatedElement duckMethod) {
		return Arrays.asList(NameChecker.class);
	}

}
