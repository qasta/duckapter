package org.duckapter.checker;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.adapter.OptionalAdapter;
import org.duckapter.annotation.Optional;

public class OptionalChecker extends DefaultChecker<Optional> {

	@Override
	protected boolean doesCheckClass(Optional anno) {
		return false;
	}
	
	@Override
	public MethodAdapter check(Optional anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (!(duck instanceof Method)) {
			return MethodAdapters.NULL;
		}
		return new OptionalAdapter(((Method)duck).getReturnType());
	}
	
	
}
