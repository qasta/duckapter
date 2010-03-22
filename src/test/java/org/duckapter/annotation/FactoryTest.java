package org.duckapter.annotation;

import static org.duckapter.DuckTestHelper.assertCanAdaptClass;
import static org.duckapter.DuckTestHelper.assertCanAdaptInstance;

import org.junit.Test;

public class FactoryTest {

	public static interface FactoryInterface {
		@Factory
		Object getInstance();
	}

	public static class WithConstructor {
		public WithConstructor() {
		}
	}

	public static class WithFactory {
		public static Object getInstance() {
			return new WithFactory();
		}
	}

	public static class WithField {
		public static Object INSTANCE = new WithField();
	}

	public static class WithoutAnyFactoryMethod {
		private WithoutAnyFactoryMethod() {
		}
	}

	@Test
	public void testFactory() {
		assertCanAdaptClass(FactoryInterface.class, WithConstructor.class,
				WithoutAnyFactoryMethod.class);
		assertCanAdaptInstance(FactoryInterface.class, WithConstructor.class,
				WithoutAnyFactoryMethod.class);

		assertCanAdaptClass(FactoryInterface.class, WithFactory.class,
				WithoutAnyFactoryMethod.class);
		assertCanAdaptInstance(FactoryInterface.class, WithFactory.class,
				WithoutAnyFactoryMethod.class);

		assertCanAdaptClass(FactoryInterface.class, WithField.class,
				WithoutAnyFactoryMethod.class);
		assertCanAdaptInstance(FactoryInterface.class, WithField.class,
				WithoutAnyFactoryMethod.class);

	}

}
