package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;

/**
 * Default checker which checks whether the target element is declared public.
 * 
 * @author Vladimir Orany
 * 
 */
public class PublicOnlyChecker extends VisibilityChecker {

	@Override
	protected Visibility getVisibility(Annotation anno) {
		return Visibility.EXACT;
	}

	@Override
	public List<Class<PublicOnlyChecker>> suppressCheckers(Annotation anno,
			AnnotatedElement duckMethod) {
		return Collections.emptyList();
	}

}
