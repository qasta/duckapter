package org.duckapter.annotation;

import org.duckapter.DuckTestHelper;
import org.junit.Test;

public class MatchingTest {

	public static interface MatchingInterface {
		@Matching("^test.*")
		void test();
	}

	public static class MatchingClass {
		public void test() {
		}
	}

	public static class NonMatchingClass {
		public void pest() {
		}
	}

	@Test
	public void testMatching() {
		DuckTestHelper.assertCanAdaptInstance(MatchingInterface.class,
				MatchingClass.class, NonMatchingClass.class);
	}

}
