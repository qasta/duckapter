package org.duckapter.checker;

import static org.duckapter.DuckTestHelper.assertCanAdaptInstance;
import static org.junit.Assert.assertEquals;

import org.duckapter.Duckapter;
import org.junit.Test;

public class ParametersTest {

	public static interface HelperInterface {
		String doIt();
	}

	public static class HelperClass {
		public String doIt() {
			return "doIt";
		}
	}

	public static interface ParametersInterface {
		String doIt(String theGood, int theBad, HelperClass theUgly);
	}

	public static class SameParameters {
		public String doIt(String theGood, int theBad, HelperClass theUgly) {
			return theGood + theBad + theUgly.doIt();
		}
	}

	public static class NearlySameParameters {
		public String doIt(CharSequence theGood, int /* long */theBad,
				HelperInterface theUgly) {
			return theGood.toString() + theBad + theUgly.doIt();
		}
	}

	@Test
	public void testParameters() {
		assertCanAdaptInstance(ParametersInterface.class, SameParameters.class,
				HelperClass.class);

		ParametersInterface pi = Duckapter.adaptInstance(new SameParameters(),
				ParametersInterface.class);
		assertEquals("good8doIt", pi.doIt("good", 8, new HelperClass()));
	}

	@Test
	public void testParameters2() {
		assertCanAdaptInstance(ParametersInterface.class,
				NearlySameParameters.class, HelperClass.class);

		ParametersInterface pi2 = Duckapter.adaptInstance(
				new NearlySameParameters(), ParametersInterface.class);
		assertEquals("good8doIt", pi2.doIt("good", 8, new HelperClass()));
	}

}
