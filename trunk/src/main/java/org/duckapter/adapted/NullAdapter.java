package org.duckapter.adapted;

final class NullAdapter implements MethodAdapter {

	public static final MethodAdapter INSTANCE = new NullAdapter();

	@Override
	public Object invoke(Object obj, Object[] args) throws Throwable {
		throw new UnsupportedOperationException(
				"Method not supported by the duck class!");
	}
	
	@Override
	public int priority() {
		return Integer.MIN_VALUE;
	}
	
	@Override
	public void replaces(MethodAdapter adapter) {}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return NullAdapter.class.equals(obj.getClass());
	}
	
	@Override
	public int hashCode() {
		return 37 + 37 * getClass().getName().hashCode();
	}
}
