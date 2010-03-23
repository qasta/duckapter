package org.duckapter.adapted;

import static org.duckapter.Duckapter.adaptInstance;
import static org.duckapter.Duckapter.canAdaptInstance;

abstract class DefaultMethodAdapter implements MethodAdapter {

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
		if (canAdaptInstance(ret, duckType)) {
			return adaptInstance(ret, duckType);
		}
		if (duckType.isPrimitive()) {
			return handlePrimitive(ret, duckType);
		}
		throw new IllegalArgumentException("Wrong object to return!");
	}

	@Override
	public MethodAdapter mergeWith(MethodAdapter other) {
		return MethodAdapters.withHighestPriority(this, other);
	}
	
	private Object handlePrimitive(Object ret, Class<?> duckType) {
		return ret;
	}

	private final Object handleReturnType(Object ret) {
		return handleObject(ret, getReturnType());
	}

}
