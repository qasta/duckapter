package org.duckapter.annotation;

import org.duckapter.DuckTestHelper;
import org.junit.Test;

public class NameTest {

	public static interface NameInterface {
		void name();
	}

	public static class NameClass {
		public void name() {
		}
	}

	public static class OtherNameClass {
		public void otherName() {
		}
	}

	@Test
	public void testName() {
		DuckTestHelper.assertCanAdaptInstance(NameInterface.class,
				NameClass.class, OtherNameClass.class);
	}

}
