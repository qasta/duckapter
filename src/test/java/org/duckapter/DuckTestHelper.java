package org.duckapter;

import static org.junit.Assert.assertFalse;

import org.duckapter.wrapper.WrapperFactory;
import org.junit.Assert;

public final class DuckTestHelper {
	private DuckTestHelper() {
	}

	/**
	 * Asserts whether the instance of the toPass class can be ducked into the
	 * tested interface but the toFail can't. Both toPass and toFail classes
	 * should be instantiated with default constructor if possible.
	 * 
	 * @param testedInterface
	 *            the tested interface
	 * @param toPass
	 *            class which can be duck typed to the test interface
	 * @param toFail
	 *            class which can't be duck typed to the test interface
	 */
	public static final void assertCanAdaptInstance(Class<?> testedInterface,
			Class<?> toPass, Class<?> toFail) {
		WrapperFactory.clearCache();
		Assert.assertNotNull(testedInterface);
		Assert.assertTrue(testedInterface.isInterface());
		Assert.assertNotNull(toPass);
		Assert.assertNotNull(toFail);

		try {
			try {
				Duck.wrap(toFail.newInstance(), testedInterface);
				Assert.fail();
			} catch (WrappingException e) {
				// ok
			}
			assertFalse(Duck.isWrappable(toFail.newInstance(),
					testedInterface));
		} catch (InstantiationException e) {
			// ok
		} catch (IllegalAccessException e) {
			// ok
		}

		try {
			try {
				Duck.wrap(toPass.newInstance(), testedInterface);
				// ok
			} catch (WrappingException e) {
				Assert.fail(e.getMessage());
			}
		} catch (InstantiationException e) {
			// ok
		} catch (IllegalAccessException e) {
			// ok
		}
	}

	/**
	 * Asserts whether the toPass class can be ducked into the tested interface
	 * but the toFail can't. Both toPass and toFail classes should be
	 * instantiated with default constructor if possible.
	 * 
	 * @param testedInterface
	 *            the tested interface
	 * @param toPass
	 *            class which can be duck typed to the test interface
	 * @param toFail
	 *            class which can't be duck typed to the test interface
	 */
	public static final void assertCanAdaptClass(Class<?> testedInterface,
			Class<?> toPass, Class<?> toFail) {
		assertFalse(Duck.isWrappable(toFail, testedInterface));

		Assert.assertNotNull(testedInterface);
		Assert.assertTrue(testedInterface.isInterface());
		Assert.assertNotNull(toPass);
		Assert.assertNotNull(toFail);

		try {
			Duck.wrap(toFail, testedInterface);
			Assert.fail();
		} catch (WrappingException e) {
			// ok
		}

		try {
			Duck.wrap(toPass, testedInterface);

		} catch (WrappingException e) {
			Assert.fail(e.getMessage());
		}

	}
}
