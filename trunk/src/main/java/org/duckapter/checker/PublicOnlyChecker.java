package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;

import org.duckapter.annotation.Visibility;

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
