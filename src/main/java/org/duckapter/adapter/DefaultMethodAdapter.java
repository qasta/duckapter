package org.duckapter.adapter;

import static org.duckapter.Duck.type;
import static org.duckapter.Duck.test;

import org.duckapter.InvocationAdapter;

public abstract class DefaultMethodAdapter implements InvocationAdapter {

	private final Class<?> returnType;

	public DefaultMethodAdapter(Class<?> returnType) {
		this.returnType = returnType;
	}

	public final Object invoke(Object obj, Object[] args) throws Throwable {
		return handleReturnType(doInvoke(obj, handleArgs(args)));
	}

	protected abstract Object doInvoke(Object obj, Object[] args)
			throws Throwable;

	protected abstract Class<?>[] getParameterTypes();

	protected final Class<?> getReturnType() {
		return returnType;
	}

	private final Object[] handleArgs(Object[] args) {
		if (args == null) {
			return new Object[0];
		}
		Object[] duckedArgs = new Object[args.length];
		Class<?>[] duckTypes = getParameterTypes();
		for (int i = 0; i < args.length; i++) {
			duckedArgs[i] = handleObject(args[i], duckTypes[i]);
		}
		return duckedArgs;
	}

	private Object handleObject(Object ret, final Class<?> duckType) {
		if (ret == null) {
			return null;
		}
		if (duckType.isAssignableFrom(ret.getClass())) {
			return ret;
		}
		if (test(ret, duckType)) {
			return type(ret, duckType);
		}
		if (duckType.isPrimitive()) {
			return handlePrimitive(ret, duckType);
		}
		throw new IllegalArgumentException("Wrong object to return!");
	}

	@Override
	public InvocationAdapter orMerge(InvocationAdapter other) {
		return InvocationAdapters.orMerge(this, other);
	}
	
	@Override
	public InvocationAdapter andMerge(InvocationAdapter other) {
		return InvocationAdapters.andMerge(this, other);
	}
	
	private Object handlePrimitive(Object ret, Class<?> duckType) {
		return ret;
	}

	private final Object handleReturnType(Object ret) {
		return handleObject(ret, getReturnType());
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
