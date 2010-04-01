package org.duckapter.adapter;

import org.duckapter.InvocationAdapter;

public class DoesNotHaveAdapter extends EmptyAdapter {

	private InvocationAdapter inverted = null;

	public DoesNotHaveAdapter(Class<?> returnType) {
		super(returnType);
	}

	@Override
	public InvocationAdapter orMerge(InvocationAdapter other) {
		if (inverted == null) {
			inverted = InvocationAdapters.MIN;
		}
		if (other instanceof DoesNotHaveAdapter) {
			inverted = inverted.orMerge(((DoesNotHaveAdapter) other).inverted);
		} else {
			inverted = inverted.orMerge(other);
		}
		return this;
	}

	@Override
	public InvocationAdapter andMerge(InvocationAdapter other) {
		if (inverted == null) {
			inverted = InvocationAdapters.MAX;
		}
		if (other instanceof DoesNotHaveAdapter) {
			inverted = inverted.andMerge(((DoesNotHaveAdapter) other).inverted);
		} else {
			inverted = inverted.andMerge(other);
		}
		return this;
	}

	@Override
	public int getPriority() {
		return InvocationAdaptersPriorities.HAS_NO;
	}

	@Override
	public boolean isInvocableOnClass() {
		return !inverted.isInvocableOnClass();
	}

	@Override
	public boolean isInvocableOnInstance() {
		return !inverted.isInvocableOnInstance();
	}

}
