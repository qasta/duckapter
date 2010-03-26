package org.duckapter.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.Checker;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;

public enum StereotypeType {
	OR {

		protected boolean checkPriority(MethodAdapter adapter,
				MethodAdapter other) {
			return other.getPriority() > adapter.getPriority();
		}

		protected MethodAdapters defaultAdapter() {
			return MethodAdapters.MIN;
		}

	},
	AND {

		protected boolean checkPriority(MethodAdapter adapter,
				MethodAdapter other) {
			return other.getPriority() < adapter.getPriority();
		}

		protected MethodAdapters defaultAdapter() {
			return MethodAdapters.MAX;
		}


	};
	public final <T extends Annotation> MethodAdapter adapt(T anno,
			AnnotatedElement original, AnnotatedElement duck,
			Map<Checker<T>, T> checkers) {
		MethodAdapter adapter = defaultAdapter();
		for (Entry<Checker<T>, T> entry : checkers.entrySet()) {
			if (!entry.getKey().canAdapt(entry.getValue(), original)) {
				continue;
			}
			MethodAdapter other = entry.getKey().adapt(entry.getValue(),
					original, duck);
			if (checkPriority(adapter, other)) {
				adapter = other;
			}
		}
		return adapter;
	}

	protected abstract MethodAdapter defaultAdapter();

	protected abstract boolean checkPriority(MethodAdapter adapter,
			MethodAdapter other);

	public final <T extends Annotation> boolean canAdapt(T anno,
			AnnotatedElement original, Map<Checker<T>, T> checkers) {
		boolean canAdapt = false;
		for (Entry<Checker<T>, T> entry : checkers.entrySet()) {
			canAdapt = canAdapt
					|| entry.getKey().canAdapt(entry.getValue(), original);
		}
		return canAdapt;
	}


}
