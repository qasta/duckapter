package org.duckapter.adapter;

import org.duckapter.MethodAdapter;

public enum MethodAdapters implements MethodAdapter {
	OK(MethodAdapterPriorities.DEFAULT){
		@Override
		public boolean isInvocableOnClass() {
			return true;
		}
		
		@Override
		public boolean isInvocableOnInstance() {
			return true;
		}
	},
	
	MAX(MethodAdapterPriorities.MAX),
	MIN(MethodAdapterPriorities.MIN),
	
	NULL(MethodAdapterPriorities.NONE) {
		@Override
		public Object invoke(Object obj, Object[] args) throws Throwable {
			throw new UnsupportedOperationException(
					"Method not supported by the duck class!");
		}
	};

	private final int  priority;
	
	private MethodAdapters(int priority) {
		this.priority = priority;
	}
	
	public static boolean isNull(MethodAdapter adapter){
		return adapter == null || NULL == adapter;
	}
	
	public static boolean notNull(MethodAdapter adapter){
		return !isNull(adapter);
	}
	
	public static MethodAdapter safe(MethodAdapter adapter, MethodAdapter defaultAdapter){
		if (adapter == null) {
			return defaultAdapter;
		}
		return adapter;
	}
	
	public static MethodAdapter orMerge(MethodAdapter first, MethodAdapter ... others){
		MethodAdapter highest = first;
		for (MethodAdapter methodAdapter : others) {
			if (methodAdapter.getPriority() > highest.getPriority()) {
				highest = methodAdapter;
			}
		}
		return highest;
	}
	
	public static MethodAdapter andMerge(MethodAdapter first, MethodAdapter ... others){
		MethodAdapter lowest = first;
		for (MethodAdapter methodAdapter : others) {
			if (methodAdapter.getPriority() < lowest.getPriority()) {
				lowest = methodAdapter;
			}
		}
		return lowest;
	}

	@Override
	public int getPriority(){
		return priority;
	}
	
	@Override
	public boolean isInvocableOnClass() {
		return false;
	}
	
	@Override
	public boolean isInvocableOnInstance() {
		return false;
	}


	@Override
	public Object invoke(Object obj, Object[] args) throws Throwable {
		return null;
	}
	
	@Override
	public MethodAdapter orMerge(MethodAdapter other) {
		return orMerge(this, other);
	}
	
	@Override
	public MethodAdapter andMerge(MethodAdapter other) {
		return andMerge(this, other);
	}
	
}
