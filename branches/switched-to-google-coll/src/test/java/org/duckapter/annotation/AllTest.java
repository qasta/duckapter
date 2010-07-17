package org.duckapter.annotation;

import static org.duckapter.Duck.type;
import static org.duckapter.DuckTestHelper.assertCanAdaptInstance;

import org.duckapter.adapted.AdaptedFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AllTest {

	public static interface TestMethod {
		void test() throws Exception;
	}

	public static interface TestCase {
		@AnnotatedWith @Before
		void setUp() throws Exception;

		@All @AnnotatedWith @Test
		TestMethod[] testMethods();

		@AnnotatedWith @After
		void tearDown() throws Exception;
	}

	public static class TestCaseImpl {
		@AnnotatedWith @Before
		public void setUp() {
		}

		@AnnotatedWith @Test
		public void test1() {
		}

		@AnnotatedWith @Test
		public void test2() {
		}

		@AnnotatedWith @Test
		public void test3() {
		}

		@AnnotatedWith @After
		public void tearDown() {
		}
	}
	
	public static class EmptyTestCase {
		@AnnotatedWith @Before
		public void setUp() {
		}

		@AnnotatedWith @After
		public void tearDown() {
		}
	}

	public static class FailClass {

	}

	@Test
	public void testTestCaseImpl() {
		assertCanAdaptInstance(TestCase.class, TestCaseImpl.class,
				FailClass.class);
		AdaptedFactory.clearCache();
		TestMethod[] testMethods = type(new TestCaseImpl(),
				TestCase.class).testMethods();
		Assert.assertEquals(3, testMethods.length);
		for (TestMethod testMethod : testMethods) {
			try {
				testMethod.test();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testEmptyTestCase() {
		assertCanAdaptInstance(TestCase.class, EmptyTestCase.class,
				FailClass.class);
		TestMethod[] testMethods = type(new EmptyTestCase(),
				TestCase.class).testMethods();
		Assert.assertEquals(0, testMethods.length);
	}

}
