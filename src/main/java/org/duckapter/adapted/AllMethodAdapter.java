package org.duckapter.adapted;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class AllMethodAdapter implements MethodAdapter {

	private static int counter = 100;
	
	private final int priority;
	
	private final Method returnsTypeMethod;
	
	
	private Collection<MethodAdapter> adapters = new LinkedHashSet<MethodAdapter>();

	public AllMethodAdapter(AnnotatedElement element, Method returnsTypeMethod) {
		System.out.println("AMA --- element: " + element + ", rtm: "
				+ returnsTypeMethod);
		priority = ++ counter;
		this.returnsTypeMethod = returnsTypeMethod;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public MethodAdapter mergeWith(MethodAdapter other) {
		System.out.println("AMA --- merge adapter " + other);
		if (!(other instanceof AllMethodAdapter) && !(other instanceof MethodAdapters) ) {
			adapters.add(other);
		}
		return this;
	}

	@Override
	public Object invoke(Object obj, Object[] args) throws Throwable {
		System.out.println("AMA --- invoke obj: " + obj + ", args: "
				+ Arrays.toString(args));
		return collectProxiesArray(obj, returnsTypeMethod.getDeclaringClass());
	}
	
	@SuppressWarnings("unchecked")
	private <T> T[] collectProxiesArray(Object obj, Class<T> footPrint){
		return collectProxies(obj, footPrint).toArray((T[])Array.newInstance(footPrint, 0));
	}
	
	private <T> List<T> collectProxies(Object obj, Class<T> footPrint){
		List<T> ret = new ArrayList<T>();
		for (MethodAdapter adapter : adapters) {
			ret.add(createProxy(adapter, obj, footPrint));
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T createProxy(final MethodAdapter adapter, final Object obj, Class<T> footPrint){
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ footPrint}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if (returnsTypeMethod.equals(method)) {
					return adapter.invoke(obj, args);
				}
				return null;
			}
		});
	}

}
