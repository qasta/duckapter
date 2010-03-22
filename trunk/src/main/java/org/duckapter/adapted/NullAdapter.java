package org.duckapter.adapted;

class NullAdapter implements MethodAdapter {

	public static final MethodAdapter INSTANCE = new NullAdapter();

	@Override
	public Object invoke(Object obj, Object[] args) throws Throwable {
		throw new UnsupportedOperationException(
				"Method not supported by the duck class!");
	}
}
