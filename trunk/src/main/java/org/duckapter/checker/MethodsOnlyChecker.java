package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;
import org.duckapter.adapter.MethodCallAdapter;

public class MethodsOnlyChecker<T extends Annotation> extends
		AbstractChecker<T> {

	public MethodAdapter adapt(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		if (original instanceof Method && duck instanceof Method ) {
			return new MethodCallAdapter((Method) duck, (Method) original);
		}
		return MethodAdapters.NULL;
	};

	protected Collection<ElementType> getTargetElements(T anno) {
		if (anno != null) {
			return super.getTargetElements(anno);
		}
		return Arrays.asList(new ElementType[] { ElementType.METHOD,
				ElementType.CONSTRUCTOR, ElementType.FIELD });
	};

}
