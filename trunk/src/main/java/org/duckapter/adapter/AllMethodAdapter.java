package org.duckapter.adapter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.duckapter.MethodAdapter;

public class AllMethodAdapter implements MethodAdapter {

	private final Method returnsTypeMethod;

	private MethodAdapter adapter = MethodAdapters.MAX;
	private AllMethodAdapter previous = null;

	public AllMethodAdapter(AnnotatedElement element, Method returnsTypeMethod) {
		this.returnsTypeMethod = returnsTypeMethod;
	}

	@Override
	public int getPriority() {
		return MethodAdapterPriorities.ALL;
	}

	@Override
	public MethodAdapter andMerge(MethodAdapter other) {
		adapter = MethodAdapters.andMerge(adapter, other);
		return this;
	}

	@Override
	public MethodAdapter orMerge(MethodAdapter theOther) {
		if (theOther.getPriority() > getPriority()) {
			return theOther;
		}
		if (theOther instanceof AllMethodAdapter) {
			this.previous = (AllMethodAdapter) theOther;
		}
		return this;
	}

	@Override
	public Object invoke(Object obj, Object[] args) throws Throwable {
		return collectProxiesArray(obj, returnsTypeMethod.getDeclaringClass());
	}

	@SuppressWarnings("unchecked")
	private <T> T[] collectProxiesArray(Object obj, Class<T> footPrint) {
		return collectProxies(obj, footPrint).toArray(
				(T[]) Array.newInstance(footPrint, 0));
	}

	private <T> List<T> collectProxies(Object obj, Class<T> footPrint) {
		List<T> ret = new ArrayList<T>();
		AllMethodAdapter ama = this;
		while (ama.previous != null) {
			if (obj == null && ama.adapter.isInvocableOnClass()) {
				ret.add(createProxy(ama.adapter, obj, footPrint));
			}
			if (obj != null && ama.adapter.isInvocableOnInstance()) {
				ret.add(createProxy(ama.adapter, obj, footPrint));
			}
			ama = ama.previous;
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <T> T createProxy(final MethodAdapter adapter, final Object obj,
			Class<T> footPrint) {
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class<?>[] { footPrint }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						if (returnsTypeMethod.equals(method)) {
							return adapter.invoke(obj, args);
						}
						return null;
					}
				});
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[return type method="
				+ returnsTypeMethod + ", adapter=" + adapter + ", previous="
				+ previous;
	}

	@Override
	public boolean isInvocableOnClass() {
		return true;
	}

	@Override
	public boolean isInvocableOnInstance() {
		return true;
	}

}
