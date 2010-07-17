package org.duckapter.checker;

import java.lang.annotation.ElementType;
import java.util.Collection;

import org.duckapter.Checker;

import com.google.common.collect.ImmutableList;

final class CheckerDescriptor {

	private final Collection<ElementType> canAdapt;
	private final int minToFail;
	private final int minToPass;
	
	@SuppressWarnings("unchecked")
	private final Collection<Class<? extends Checker>>  suppressed;
	
	@SuppressWarnings("unchecked")
	public CheckerDescriptor(Collection<ElementType> canAdapt,
			Collection<Class<? extends Checker>>  suppressed, int minToFail, int minToPass) {
		this.canAdapt = ImmutableList.copyOf(canAdapt);
		this.suppressed = ImmutableList.copyOf(suppressed);
		this.minToFail = minToFail;
		this.minToPass = minToPass;
	}

	public Collection<ElementType> getCanAdapt() {
		return canAdapt;
	}

	public int getMinToFail() {
		return minToFail;
	}

	public int getMinToPass() {
		return minToPass;
	}

	@SuppressWarnings("unchecked")
	public Collection<Class<? extends Checker>>  getSuppressed() {
		return suppressed;
	}
	
	
	
	
	
}
