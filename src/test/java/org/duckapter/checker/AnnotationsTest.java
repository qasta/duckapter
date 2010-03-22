package org.duckapter.checker;

import static org.duckapter.DuckTestHelper.assertCanAdaptInstance;

import org.junit.Test;

public class AnnotationsTest {

	public static interface AnnotatedInterface {
		@Test
		void testMethod();
	}

	public static class AnnotatedClass {
		@Test
		public void anyMethodName() {
		}
	}

	public static class NotAnnotatedClass {
		public void testMethod() {
		}
	}

	@Test
	public void testAnnotations() {
		assertCanAdaptInstance(AnnotatedInterface.class, AnnotatedClass.class,
				NotAnnotatedClass.class);
	}
}
