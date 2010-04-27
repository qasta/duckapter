package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Map.Entry;

import org.duckapter.Checker;
import org.duckapter.InvocationAdapter;
import org.duckapter.adapter.InvocationAdapters;
import org.duckapter.annotation.StereotypeChecker;

/**
 * Stereotype type declares the way how the {@link StereotypeChecker} determines
 * the successful check. In case of {@link #OR} the check succeed if one or more
 * checkers got for the checker annotations used on the newly declared
 * annotation succeed. In case of {@link #AND} all checks must pass.
 * 
 * @author Vladimir Orany
 */
public enum StereotypeType {

	/**
	 * In OR mode one successful check is enough to pass the whole check.
	 */
	OR {

		protected boolean checkPriority(InvocationAdapter adapter,
				InvocationAdapter other) {
			return other.getPriority() > adapter.getPriority();
		}

		protected InvocationAdapters defaultAdapter() {
			return InvocationAdapters.MIN;
		}

	},
	/**
	 * In AND mode all check must be successful to pass the check.
	 */
	AND {

		protected boolean checkPriority(InvocationAdapter adapter,
				InvocationAdapter other) {
			return other.getPriority() < adapter.getPriority();
		}

		protected InvocationAdapters defaultAdapter() {
			return InvocationAdapters.MAX;
		}

	};

	final <T extends Annotation> InvocationAdapter adapt(T anno,
			AnnotatedElement original, AnnotatedElement duck,
			Class<?> classOfOriginal, Map<Checker<T>, T> checkers) {
		InvocationAdapter adapter = defaultAdapter();
		for (Entry<Checker<T>, T> entry : checkers.entrySet()) {
			if (!entry.getKey().canAdapt(entry.getValue(), original,
					classOfOriginal)) {
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

	final <T extends Annotation> boolean canAdapt(T anno,
			AnnotatedElement original, Class<?> classOfOriginal,
			Map<Checker<T>, T> checkers) {
		boolean canAdapt = false;
		for (Entry<Checker<T>, T> entry : checkers.entrySet()) {
			canAdapt = canAdapt
					|| entry.getKey().canAdapt(entry.getValue(), original,
							classOfOriginal);
		}
		return canAdapt;
	}

}
