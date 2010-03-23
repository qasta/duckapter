package org.duckapter.adapted;

public enum MethodAdapters implements MethodAdapter {
	OK{
		@Override
		public Object invoke(Object obj, Object[] args) throws Throwable {
			return null;
		}

		@Override
		public int getPriority() {
			return Integer.MIN_VALUE + 1;
		}

		@Override
		public MethodAdapter mergeWith(MethodAdapter other) {
			return withHighestPriority(this, other);
		}
		
	},
	NULL {
		@Override
		public Object invoke(Object obj, Object[] args) throws Throwable {
			throw new UnsupportedOperationException(
					"Method not supported by the duck class!");
		}
		
		@Override
		public int getPriority() {
			return Integer.MIN_VALUE;
		}
		
		@Override
		public MethodAdapter mergeWith(MethodAdapter other) {
			return other;
		}
	};

	public static boolean isNull(MethodAdapter adapter){
		return adapter == null || NULL == adapter;
	}
	
	public static boolean notNull(MethodAdapter adapter){
		return !isNull(adapter);
	}
	
	public static MethodAdapter safe(MethodAdapter adapter){
		if (adapter == null) {
			return NULL;
		}
		return adapter;
	}
	
	public static MethodAdapter withHighestPriority(MethodAdapter first, MethodAdapter ... others){
		MethodAdapter highest = first;
		for (MethodAdapter methodAdapter : others) {
			if (methodAdapter.getPriority() > highest.getPriority()) {
				highest = methodAdapter;
			}
		}
		return highest;
	}
	
	@Override
	public abstract Object invoke(Object obj, Object[] args) throws Throwable;

	@Override
	public abstract int getPriority();

	@Override
	public abstract MethodAdapter mergeWith(MethodAdapter other);
	
	
}
