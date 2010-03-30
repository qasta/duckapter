package org.duckapter.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;

public enum StereotypeType {
	OR {

		protected boolean checkPriority(InvocationAdapter adapter,
				InvocationAdapter other) {
			return other.getPriority() > adapter.getPriority();
		}

		protected InvocationAdapters defaultAdapter() {
			return InvocationAdapters.MIN;
		}

	},
	AND {

		protected boolean checkPriority(InvocationAdapter adapter,
				InvocationAdapter other) {
			return other.getPriority() < adapter.getPriority();
		}

		protected InvocationAdapters defaultAdapter() {
			return InvocationAdapters.MAX;
		}


	};
	public final <T extends Annotation> InvocationAdapter adapt(T anno,
			AnnotatedElement original, AnnotatedElement duck, Class<?> classOfOriginal, 
			Map<Checker<T>, T> checkers) {
		InvocationAdapter adapter = defaultAdapter();
		for (Entry<Checker<T>, T> entry : checkers.entrySet()) {
			if (!entry.getKey().canAdapt(entry.getValue(), original, classOfOriginal)) {
				continue;
			}
			InvocationAdapter other = entry.getKey().adapt(entry.getValue(),
					original, duck, classOfOriginal);
			if (checkPriority(adapter, other)) {
				adapter = other;
			}
		}
		return adapter;
	}

	protected abstract InvocationAdapter defaultAdapter();

	protected abstract boolean checkPriority(InvocationAdapter adapter,
			InvocationAdapter other);

	public final <T extends Annotation> boolean canAdapt(T anno,
			AnnotatedElement original, Class<?> classOfOriginal, Map<Checker<T>, T> checkers) {
		boolean canAdapt = false;
		for (Entry<Checker<T>, T> entry : checkers.entrySet()) {
			canAdapt = canAdapt
					|| entry.getKey().canAdapt(entry.getValue(), original, classOfOriginal);
		}
		return canAdapt;
	}


}
