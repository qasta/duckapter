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
		@Before
		void setUp() throws Exception;

		@All @Test
		TestMethod[] testMethods();

		@After
		void tearDown() throws Exception;
	}

	public static class TestCaseImpl {
		@Before
		public void setUp() {
		}

		@Test
		public void test1() {
		}

		@Test
		public void test2() {
		}

		@Test
		public void test3() {
		}

		@After
		public void tearDown() {
		}
	}
	
	public static class EmptyTestCase {
		@Before
		public void setUp() {
		}

		@After
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
