package org.duckapter.adapter;

import static org.duckapter.Duck.type;
import static org.duckapter.Duck.test;

import org.duckapter.Adapted;
import org.duckapter.InvocationAdapter;

/**
 * This class helps creating new implementations of {@link InvocationAdapter}
 * interface. It provides methods for handling input parameters and also the
 * return value. Merge methods {@link #orMerge(InvocationAdapter)} and
 * {@link #andMerge(InvocationAdapter)} delegate its function to the
 * {@link InvocationAdapters} implementations. The methods
 * {@link #isInvocableOnClass()} and {@link #isInvocableOnInstance()} return
 * <code>true</code> by default.
 * 
 * @author Vladimir Orany
 * 
 */
public abstract class AbstractInvocationAdapter implements InvocationAdapter {

	private final Class<?> returnType;

	/**
	 * @param returnType the return type of the invocation
	 */
	public AbstractInvocationAdapter(Class<?> returnType) {
		this.returnType = returnType;
	}

	public final Object invoke(Object obj, Object[] args) throws Throwable {
		return handleReturnType(doInvoke(obj, handleArgs(args)));
	}

	/**
	 * Perform the invocation on the adapted element.
	 * @param obj the object to be the invocation performed
	 * @param args prepared method arguments
	 * @return result of the invocation to be handled by this class and returned
	 * @throws Throwable if exception occurs during the invocation
	 */
	protected abstract Object doInvoke(Object obj, Object[] args)
			throws Throwable;

	/**
	 * @return the original parameter types for the invocation
	 */
	protected abstract Class<?>[] getParameterTypes();

	/**
	 * @return the return type of the duck method
	 */
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

	@SuppressWarnings("unchecked")
	private Object handleObject(Object ret, final Class<?> duckType) {
		if (ret == null) {
			return null;
		}
		if (ret instanceof Adapted) {
			Adapted adapted = (Adapted) ret;
			if (duckType.isAssignableFrom(adapted.getAdaptedClass()
					.getOriginalClass())) {
				return adapted.getOriginalInstance();
			}

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

	public InvocationAdapter orMerge(InvocationAdapter other) {
		return InvocationAdapters.orMerge(this, other);
	}

	public InvocationAdapter andMerge(InvocationAdapter other) {
		return InvocationAdapters.andMerge(this, other);
	}

	private Object handlePrimitive(Object ret, Class<?> duckType) {
		return ret;
	}

	private final Object handleReturnType(Object ret) {
		return handleObject(ret, getReturnType());
	}

	public boolean isInvocableOnClass() {
		return true;
	}

	public boolean isInvocableOnInstance() {
		return true;
	}

}
