package org.duckapter.adapter;

import org.duckapter.InvocationAdapter;

public enum InvocationAdapters implements InvocationAdapter {
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
	
	private InvocationAdapters(int priority) {
		this.priority = priority;
	}
	
	public static boolean isNull(InvocationAdapter adapter){
		return adapter == null || NULL == adapter;
	}
	
	public static boolean notNull(InvocationAdapter adapter){
		return !isNull(adapter);
	}
	
	public static InvocationAdapter safe(InvocationAdapter adapter, InvocationAdapter defaultAdapter){
		if (adapter == null) {
			return defaultAdapter;
		}
		return adapter;
	}
	
	public static InvocationAdapter orMerge(InvocationAdapter first, InvocationAdapter ... others){
		InvocationAdapter highest = first;
		for (InvocationAdapter invocationAdapter : others) {
			if (invocationAdapter.getPriority() > highest.getPriority()) {
				highest = invocationAdapter;
			}
		}
		return highest;
	}
	
	public static InvocationAdapter andMerge(InvocationAdapter first, InvocationAdapter ... others){
		InvocationAdapter lowest = first;
		for (InvocationAdapter invocationAdapter : others) {
			if (invocationAdapter.getPriority() < lowest.getPriority()) {
				lowest = invocationAdapter;
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
	public InvocationAdapter orMerge(InvocationAdapter other) {
		return orMerge(this, other);
	}
	
	@Override
	public InvocationAdapter andMerge(InvocationAdapter other) {
		return andMerge(this, other);
	}
	
}