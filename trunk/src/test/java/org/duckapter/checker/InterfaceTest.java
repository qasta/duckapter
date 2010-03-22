package org.duckapter.checker;

import org.duckapter.Duckapter;
import org.junit.Assert;
import org.junit.Test;

public class InterfaceTest {

	public static interface TheInterface {
	}

	public static interface OtherInterface {
	}

	public static class TheClass {
	}

	@Test
	public void testAdaptClass() {
		Assert.assertFalse(Duckapter.canAdaptClass(TheInterface.class,
				TheClass.class));
	}

	@Test
	public void testAdaptInterface() {
		Assert.assertFalse(Duckapter.canAdaptInstance(TheInterface.class,
				TheClass.class));
	}
	
}
