package org.duckapter.checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.duckapter.Checker;
import org.duckapter.MethodAdapter;
import org.duckapter.adapter.MethodAdapters;

public abstract class DefaultChecker<T extends Annotation> implements
		Checker<T> {

	private static final Collection<Checker<?>> defaultCheckers = new ArrayList<Checker<?>>();

	public static final Collection<Checker<?>> getDefaultCheckers() {
		return defaultCheckers;
	}

	static {
		defaultCheckers.add(new AnnotationsChecker<Annotation>());
		defaultCheckers.add(new ExceptionsChecker<Annotation>());
		defaultCheckers.add(new NameChecker<Annotation>());
		defaultCheckers.add(new MethodsOnlyChecker<Annotation>());
		defaultCheckers.add(new PublicOnlyChecker<Annotation>());
		defaultCheckers.add(new ConcreteMethodsChecker<Annotation>());
		// dva nejproblematictejsi, protoze obsahuji rekuzni volani
		defaultCheckers.add(new ParametersChecker<Annotation>());
		defaultCheckers.add(new ReturnTypeChecker<Annotation>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean doesCheck(T anno, AnnotatedElement element) {
		if (element instanceof Class) {
			return doesCheckClass(anno);
		}
		if (element instanceof Method) {
			return doesCheckMethod(anno);
		}
		if (element instanceof Field) {
			return doesCheckField(anno);
		}
		if (element instanceof Constructor) {
			return doesCheckConstructor(anno);
		}
		// not implemented yet
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MethodAdapter check(T anno, AnnotatedElement original,
			AnnotatedElement duck) {
		// setAccessible(original);
		// setAccessible(duck);
		if (original instanceof Class && duck instanceof Class) {
			return adaptClass(anno, (Class) original, (Class) duck);
		}
		if (duck instanceof Method) {
			Method duckMethod = (Method) duck;
			if (original instanceof Field) {
				return adaptField(anno, (Field) original, duckMethod);
			}
			if (original instanceof Method) {
				return adaptMethod(anno, (Method) original, duckMethod);
			}
			if (original instanceof Constructor) {
				return adaptConstructor(anno, (Constructor) original,
						duckMethod);
			}
		}
		return MethodAdapters.NULL;
	}
	
	

	// private void setAccessible(AnnotatedElement original) {
	// if (original instanceof AccessibleObject) {
	// AccessibleObject ao = (AccessibleObject) original;
	// ao.setAccessible(true);
	// }
	// };

	protected MethodAdapter adaptClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return toMethodAdapter(checkClass(anno, clazz, duckInterface));
	}

	protected MethodAdapter adaptField(T anno, Field field, Method duckMethod) {
		return toMethodAdapter(checkField(anno, field, duckMethod));
	}

	protected MethodAdapter adaptMethod(T anno, Method method, Method duckMethod) {
		return toMethodAdapter(checkMethod(anno, method, duckMethod));
	}

	protected MethodAdapter adaptConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return toMethodAdapter(checkConstructor(anno, constructor, duckMethod));
	};
	
	protected boolean checkClass(T anno, Class<?> clazz, Class<?> duckInterface) {
		return false;
	}

	protected boolean checkField(T anno, Field field, Method duckMethod) {
		return false;
	}

	protected boolean checkMethod(T anno, Method method, Method duckMethod) {
		return false;
	}

	protected boolean checkConstructor(T anno, Constructor<?> constructor,
			Method duckMethod) {
		return false;
	};

	protected boolean doesCheckClass(T anno) {
		return true;
	}

	protected boolean doesCheckField(T anno) {
		return true;
	}

	protected boolean doesCheckMethod(T anno) {
		return true;
	}

	protected boolean doesCheckConstructor(T anno) {
		return true;
	};

	protected MethodAdapter toMethodAdapter(boolean b){
		return booleanToMethodAdapter(b, MethodAdapters.OK);
	}
	
	protected MethodAdapter booleanToMethodAdapter(boolean b, MethodAdapter okAdapter){
		return b ? okAdapter : MethodAdapters.NULL;
	}
	
	@Override
	public <A extends Annotation, Ch extends Checker<A>> Collection<Class<Ch>> suppressCheckers(
			AnnotatedElement duckMethod) {
		return Collections.emptyList();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return obj.getClass().equals(getClass());
	}

	@Override
	public int hashCode() {
		return 37 + 37 * getClass().getName().hashCode();
	}
}
